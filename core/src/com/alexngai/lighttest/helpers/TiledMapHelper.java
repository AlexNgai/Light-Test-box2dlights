package com.alexngai.lighttest.helpers;

import java.util.ArrayList;
import java.util.HashMap;

import com.alexngai.lighttest.gameobjects.ObjectTable;
//import com.alexngai.lighttest.helpers.TiledMapHelper.LineSegment;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public class TiledMapHelper {

	public static float mapscale = .1f;
	private OrthographicCamera camera;

	private TiledMapRenderer tiledMapRenderer;
	private TiledMap map;
	
	private Body groundBody;
	
	public TiledMapHelper(OrthographicCamera camera){
		map = new TmxMapLoader().load("testmap1.tmx");
		
		tiledMapRenderer = new OrthogonalTiledMapRenderer(map, mapscale);
		this.camera = camera;
	}
	
	public TiledMapHelper(OrthographicCamera camera, String mapFile){
		map = new TmxMapLoader().load(mapFile);
		
		tiledMapRenderer = new OrthogonalTiledMapRenderer(map, mapscale);
		this.camera = camera;
	}
	
	public void render(){
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
	}
	
	public TiledMap getMap() {
		return map;
	}
	
	public void loadCollisions(String collisionsFile, World world,
			float pixelsPerMeter) {
		
		//get map properties
		MapProperties prop = map.getProperties();
		int mapWidth = prop.get("width", Integer.class);
		int mapHeight = prop.get("height", Integer.class);
		int tilePixelWidth = prop.get("tilewidth", Integer.class);
		int tilePixelHeight = prop.get("tileheight", Integer.class);
		int mapPixelWidth = mapWidth * tilePixelWidth;
		int mapPixelHeight = mapHeight * tilePixelHeight;
		Gdx.app.log("Map", "width: " + mapWidth + ", height:" + mapHeight + "tilewidth: " + tilePixelWidth + "mapPixelWidth:" + mapPixelWidth);
		
		FileHandle fh = Gdx.files.internal(collisionsFile);
		String collisionFile = fh.readString();
		String lines[] = collisionFile.split("\\r?\\n");

		for (int n = 0; n < lines.length; n++) {
			String cols[] = lines[n].split(" ");
			int tileNo = Integer.parseInt(cols[0]);

			ArrayList<LineSegment> tmp = new ArrayList<LineSegment>();
		
		
		}

		HashMap<Integer, ArrayList<LineSegment>> tileCollisionJoints = new HashMap<Integer, ArrayList<LineSegment>>();

		/**
		 * Some locations on the map (perhaps most locations) are "undefined",
		 * empty space, and will have the tile type 0. This code adds an empty
		 * list of line segments for this "default" tile.
		 */
		tileCollisionJoints.put(Integer.valueOf(0),
				new ArrayList<LineSegment>());

		for (int n = 0; n < lines.length; n++) {
			String cols[] = lines[n].split(" ");
			int tileNo = Integer.parseInt(cols[0]);

			ArrayList<LineSegment> tmp = new ArrayList<LineSegment>();

			//add all pair coordinates to list
			for (int m = 1; m < cols.length; m++) {
				String coords[] = cols[m].split(",");

				String start[] = coords[0].split("x");
				String end[] = coords[1].split("x");

				tmp.add(new LineSegment(Integer.parseInt(start[0]), Integer
						.parseInt(start[1]), Integer.parseInt(end[0]), Integer
						.parseInt(end[1])));
			}

			tileCollisionJoints.put(Integer.valueOf(tileNo), tmp);
		}

		ArrayList<LineSegment> collisionLineSegments = new ArrayList<LineSegment>();

		for (int y = 0; y < mapHeight; y++) {
			for (int x = 0; x < mapWidth; x++) {
				TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
				int tileType = layer.getCell((mapHeight-1)-y,x).getTile().getId();
				//int tileType = map.layers(0).tiles[(mapHeight - 1) - y][x];

				for (int n = 0; n < tileCollisionJoints.get(
						Integer.valueOf(tileType)).size(); n++) {
					LineSegment lineSeg = tileCollisionJoints.get(
							Integer.valueOf(tileType)).get(n);

					addOrExtendCollisionLineSegment(x * tilePixelWidth
							- lineSeg.start().y + tilePixelHeight, y * tilePixelHeight
							- lineSeg.start().x + tilePixelWidth, x * tilePixelWidth
							- lineSeg.end().y + tilePixelHeight, y * tilePixelHeight
							- lineSeg.end().x + tilePixelWidth, collisionLineSegments);
				}
			}
		}

		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyDef.BodyType.StaticBody;
		groundBody = world.createBody(groundBodyDef);
		for (LineSegment lineSegment : collisionLineSegments) {
			EdgeShape environmentShape = new EdgeShape();
			environmentShape.set(
					lineSegment.start().scl(1/ pixelsPerMeter), lineSegment
							.end().scl(1 / pixelsPerMeter));
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}

		/**
		 * Drawing a boundary around the entire map. We can't use a box because
		 * then the world objects would be inside and the physics engine would
		 * try to push them out.
		 */

		EdgeShape mapBounds = new EdgeShape();
		mapBounds.set(new Vector2(0.0f, 0.0f), new Vector2(mapPixelWidth
				/ pixelsPerMeter, 0.0f));
		groundBody.createFixture(mapBounds, 0);

		mapBounds.set(new Vector2(0.0f, mapPixelHeight / pixelsPerMeter),
				new Vector2(mapPixelWidth / pixelsPerMeter, mapPixelHeight
						/ pixelsPerMeter));
		groundBody.createFixture(mapBounds, 0);

		mapBounds.set(new Vector2(0.0f, 0.0f), new Vector2(0.0f,
				mapPixelHeight / pixelsPerMeter));
		groundBody.createFixture(mapBounds, 0);

		mapBounds.set(new Vector2(mapPixelWidth / pixelsPerMeter, 0.0f),
				new Vector2(mapPixelWidth / pixelsPerMeter, mapPixelHeight
						/ pixelsPerMeter));
		groundBody.createFixture(mapBounds, 0);
		
		for (Fixture f : groundBody.getFixtureList()){
			ObjectTable.addObjectType(f.hashCode(), ObjectTable.wallType);
		}

		mapBounds.dispose();
		
	}
	
	//new line segment addition
	private void addOrExtendCollisionLineSegment(float lsx1, float lsy1,
			float lsx2, float lsy2, ArrayList<LineSegment> collisionLineSegments) {
		LineSegment line = new LineSegment(lsx1, lsy1, lsx2, lsy2);

		boolean didextend = false;

		for (LineSegment test : collisionLineSegments) {
			if (test.extendIfPossible(line)) {
				didextend = true;
				break;
			}
		}

		if (!didextend) {
			collisionLineSegments.add(line);
		}
	}
	
	private class LineSegment {
		private Vector2 start = new Vector2();
		private Vector2 end = new Vector2();

		/**
		 * Construct a new LineSegment with the specified coordinates.
		 * 
		 * @param x1
		 * @param y1
		 * @param x2
		 * @param y2
		 */
		public LineSegment(float x1, float y1, float x2, float y2) {
			start = new Vector2(x1, y1);
			end = new Vector2(x2, y2);
		}

		/**
		 * The "start" of the line. Start and end are misnomers, this is just
		 * one end of the line.
		 * 
		 * @return Vector2
		 */
		public Vector2 start() {
			return start;
		}

		/**
		 * The "end" of the line. Start and end are misnomers, this is just one
		 * end of the line.
		 * 
		 * @return Vector2
		 */
		public Vector2 end() {
			return end;
		}

		/**
		 * Determine if the requested line could be tacked on to the end of this
		 * line with no kinks or gaps. If it can, the current LineSegment will
		 * be extended by the length of the passed LineSegment.
		 * 
		 * @param lineSegment
		 * @return boolean true if line was extended, false if not.
		 */
		public boolean extendIfPossible(LineSegment lineSegment) {
			/**
			 * First, let's see if the slopes of the two segments are the same.
			 */
			double slope1 = Math.atan2(end.y - start.y, end.x - start.x);
			double slope2 = Math.atan2(lineSegment.end.y - lineSegment.start.y,
					lineSegment.end.x - lineSegment.start.x);

			if (Math.abs(slope1 - slope2) > 1e-9) {
				return false;
			}

			/**
			 * Second, check if either end of this line segment is adjacent to
			 * the requested line segment. So, 1 pixel away up through sqrt(2)
			 * away.
			 * 
			 * Whichever two points are within the right range will be "merged"
			 * so that the two outer points will describe the line segment.
			 */
			if (start.dst(lineSegment.start) <= Math.sqrt(2) + 1e-9) {
				start.set(lineSegment.end);
				return true;
			} else if (end.dst(lineSegment.start) <= Math.sqrt(2) + 1e-9) {
				end.set(lineSegment.end);
				return true;
			} else if (end.dst(lineSegment.end) <= Math.sqrt(2) + 1e-9) {
				end.set(lineSegment.start);
				return true;
			} else if (start.dst(lineSegment.end) <= Math.sqrt(2) + 1e-9) {
				start.set(lineSegment.start);
				return true;
			}

			return false;
		}

		/**
		 * Returns a pretty description of the LineSegment.
		 * 
		 * @return String
		 */
		@Override
		public String toString() {
			return "[" + start.x + "x" + start.y + "] -> [" + end.x + "x"
					+ end.y + "]";
		}
	}
	
	public Body getGroundBody(){
		return groundBody;
	}
	
}
