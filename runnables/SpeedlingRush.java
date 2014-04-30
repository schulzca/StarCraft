package runnables;
import java.awt.Point;
import java.awt.event.KeyEvent;

import utilities.AI;
import utilities.GameObject;
import utilities.Resource;
import buildings.Hatchery;


public class SpeedlingRush {
	
	
	private static final double /*SLOWER = 1.66,
								SLOW = 1.25, 
								NORMAL = 1,
								FAST = 0.8275, */
								FASTER = 0.725;
	
	private static final double GAME_SPEED = FASTER;
	private static boolean buildOnLeft = true;
	private static int width, height;
	private static final int TILE_X = 40, TILE_Y = 33;
	public static Point SPAWNINGPOOL, HATCHERY2, EXTRACTOR,
						ENEMY, EXTRACTOR2, RALLY, RALLY2, BASE,
						MINERAL;
	
	//Commonly used variables
	private static Resource min25 = new Resource(Resource.MINERALS, 25),
							min50 = new Resource(Resource.MINERALS, 50),
							min100 = new Resource(Resource.MINERALS, 100),
							min150 = new Resource(Resource.MINERALS, 150),
							min200 = new Resource(Resource.MINERALS, 200),
							vesp100 = new Resource(Resource.VESPENE, 100);
	
	
	public static void main(String[] args) {
		AI ai = new AI();
		width = ai.getWidth();
		height = ai.getHeight();

		//Switch to game
		ai.altTab();
		ai.sleep(0.5);
		//Wait for game to start
		ai.waitFor(min50);

		//GROUP 1 <- hatchery
		ai.type(KeyEvent.VK_BACK_SPACE);
		ai.sleep(0.3);
		ai.leftClick(width/2, height/2 - 100);
		ai.assignToGroup(1);
		
		//4 drones
		for(int i = 0; i < 4; i++){
			makeDrone(ai);	
		}
		
		//Overlord
		makeOverlord(ai);
		
		
		//5 drones
		for(int i = 0; i < 5; i++){
			makeDrone(ai);
		}
		//Determine which base we're at
		//and setup accordingly
		buildOnLeft = ai.isAtBottomBase();
		if(buildOnLeft){ 
			SPAWNINGPOOL = new Point(1065, 220);
			EXTRACTOR = new Point(width/2 + 100, 100);
			EXTRACTOR2 = new Point(width/2 + 100, 575);
			HATCHERY2 = new Point(460, 320);
			ENEMY = new Point(70, 590);
			RALLY = new Point(118, 716);
			RALLY2 = new Point(130, 610);
			BASE = new Point(149, 737);
			MINERAL = new Point(960, 275);
		} else {
			SPAWNINGPOOL = new Point(270, 400);
			EXTRACTOR = new Point(width/2 - 50, 100);
			EXTRACTOR2 = new Point(width/2 - 50, 575);
			HATCHERY2 = new Point(900, 320);
			ENEMY = new Point(150, 740);
			RALLY = new Point(105, 615);
			RALLY2 = new Point(100, 725);
			BASE = new Point(73, 595);
			MINERAL = new Point(475, 360);
		}
		
		//BUILD Extractor
		buildExtractor(ai, 1);
		long extractorStart = System.currentTimeMillis();
		
		//BUILD DRONE
		makeDrone(ai);
		
		//SPAWNING POOL
		buildSpawningPool(ai);
		
		//BUILD DRONE
		makeDrone(ai);
		
		//Wait for extractor to finish
		while(System.currentTimeMillis() - extractorStart < realToGameTime(28000));
		
		//Send 3 drones to extractor
		for(int i = 1; i <= 3; i++){
			ai.selectAll();
			ai.sleep(0.1);
			ai.leftClick(getArmyUnit(i + 2));
			ai.assignToGroup(i + 2);
			ai.rightClick(EXTRACTOR);
		}
		
		//BUILD Hatchery
		ai.selectAll();
		ai.sleep(0.1);
		ai.leftClick(getArmyUnit(6));
		ai.type(KeyEvent.VK_B);
		ai.waitFor(new Resource(Resource.MINERALS, 270));
		ai.rightClick(HATCHERY2);
		ai.waitFor(new Resource(Resource.MINERALS, 300));
		ai.type(KeyEvent.VK_H);
		ai.leftClick(HATCHERY2);		
		
		//pull workers off gas
		ai.waitFor(vesp100);
		for(int times = 0; times < 10; times++){
			for(int i = 1; i <= 3; i++){
				ai.selectGroup(i + 2);
				ai.rightClick(MINERAL);
			}
			ai.sleep(0.1);
		}
		
		//BUILD QUEEN
		ai.leftClick(SPAWNINGPOOL);
		ai.sleep(0.1);
		while(!ai.canClickCommand(0, 0));
		ai.selectGroup(1);
		ai.waitFor(min150);
		ai.rightClick(RALLY);
		ai.type(KeyEvent.VK_Q);
		ai.sleep(0.1);
		
		//RESEARCH BOOST
		ai.leftClick(SPAWNINGPOOL);
		ai.waitFor(min100);
		ai.waitFor(vesp100);
		ai.type(KeyEvent.VK_M);
		
		//make zergling
		makeZergling(ai);

		//make overlord
		makeOverlord(ai);
		
		//make zergling
		makeZergling(ai);
		
		//Group 2 <- Queen
		ai.selectGroup(1);
		ai.sleep(0.3);
		while(ai.canClickCommand(4, 2));
		ai.sleep(0.3);
		ai.selectAll();
		ai.sleep(0.3);
		ai.leftClick(getArmyUnit(1));
		ai.assignToGroup(2);
		
		//Inject
		ai.type(KeyEvent.VK_V);
		ai.leftClick(BASE);
		
		//Group 1 <- both hatches
		ai.selectGroup(1);
		ai.shiftLeftClick(HATCHERY2);
		ai.assignToGroup(1);
		ai.rightClick(RALLY);
		
		//make zerglings
		for(int i = 0; i < 12; i++){
			makeZergling(ai);
		}
		
		ai.sleep(realToGameTime(24));
		ai.selectGroup(1);
		ai.rightClick(ENEMY);
		ai.type(KeyEvent.VK_F2);
		ai.type(KeyEvent.VK_A);
		ai.leftClick(ENEMY);
		
		
		while(ai.getResourceAmount(Resource.MAXSUPPLY) > 0){
			//Watch
			ai.type(KeyEvent.VK_F2);
			ai.type(KeyEvent.VK_F2);
			ai.type(KeyEvent.VK_A);
			ai.leftClick(ENEMY);
			ai.sleep(0.5);
			
			//Reinforce
			tryToMakeZergling(ai);
			ai.type(KeyEvent.VK_F2);
			ai.type(KeyEvent.VK_F2);
			ai.sleep(0.3);
			//Inject
//			tryToInject(ai);
//			ai.type(KeyEvent.VK_F2);
//			ai.type(KeyEvent.VK_F2);
//			ai.sleep(0.3);
			//Make overlord if needed
			makeOverlordIfNeeded(ai);
			ai.type(KeyEvent.VK_F2);
			ai.type(KeyEvent.VK_F2);
			ai.sleep(0.3);
			//ACCEPT SURRENDER
			ai.leftClick(new Point(1160, 120));
		
		}
		
	}
	
	private static Point getSupply(int num){
		int useWidth = 1;
		int reverse = 1;
		if(buildOnLeft){
			useWidth = 0;
			reverse = -1;
		}
		num--;
		int x = 225;
		int y = 100;
		int xOffset = 2*TILE_X * (num % 4) - 10 * ( num/4 );
		int yOffset = 2*TILE_Y * (num / 4);
		return new Point(useWidth * width - (x+xOffset) * reverse , y+yOffset);
	}
	
	private static void tryToInject(AI ai){
		ai.selectGroup(1);
		ai.sleep(0.1);
		if(!GameObject.isSelected(ai.screenShot(), "Injected")){
			ai.selectGroup(2);
			ai.sleep(0.1);
			ai.type(KeyEvent.VK_V);
			ai.sleep(0.3);
			if(ai.canClickCommand(4, 2)){
				ai.leftClick(BASE);
			}
		}
	}
	
	private static void tryToMakeZergling(AI ai){
		ai.selectGroup(1);
		ai.sleep(0.3);
		if(GameObject.hasLarva(ai.screenShot())){
			if(ai.getResourceAmount(Resource.MINERALS) >= 50){
				if(ai.getResourceAmount(Resource.CURSUPPLY) != 
						ai.getResourceAmount(Resource.MAXSUPPLY)){
					ai.type(KeyEvent.VK_S);
					ai.type(KeyEvent.VK_Z);
				}
			}
		}
	}
	
	private static void makeDrone(AI ai){
		ai.selectGroup(1);
		ai.sleep(0.1);
		while(!GameObject.hasLarva(ai.screenShot()));
		ai.waitFor(min50);
		while(ai.getResourceAmount(Resource.CURSUPPLY) == 
				ai.getResourceAmount(Resource.MAXSUPPLY));
		ai.type(KeyEvent.VK_S);
		ai.type(KeyEvent.VK_D);
	}
	
	private static void makeZergling(AI ai){
		
		ai.selectGroup(1);
		ai.sleep(0.1);
		while(ai.getResourceAmount(Resource.CURSUPPLY) == 
				ai.getResourceAmount(Resource.MAXSUPPLY));
		ai.waitFor(min50);
		while(!GameObject.hasLarva(ai.screenShot()));
		ai.type(KeyEvent.VK_S);
		ai.type(KeyEvent.VK_Z);
	}
	
	private static void makeOverlord(AI ai){
		ai.selectGroup(1);
		ai.sleep(0.1);
		ai.waitFor(min100);
		while(!GameObject.hasLarva(ai.screenShot()));
		ai.type(KeyEvent.VK_S);
		ai.type(KeyEvent.VK_V);
	}
	
	private static void makeOverlordIfNeeded(AI ai){
		ai.selectGroup(1);
		ai.sleep(0.1);
		if(GameObject.hasLarva(ai.screenShot())){
			if(ai.getResourceAmount(Resource.MINERALS) >= 100){
				if(ai.getResourceAmount(Resource.CURSUPPLY) >= 
						ai.getResourceAmount(Resource.MAXSUPPLY) - 1){
					ai.type(KeyEvent.VK_S);
					ai.type(KeyEvent.VK_V);
				}
			}
		}
	}
	
	private static boolean selectLarva(AI ai){
		Hatchery h = new Hatchery(0,0,0,0);
		ai.selectGroup(1);
		ai.type(KeyEvent.VK_S);
		ai.sleep(0.3);
		return !h.isSelected(ai.screenShot());
	}
	
	private static void buildExtractor(AI ai, int num){
		ai.selectAll();
		ai.waitFor(new Resource(Resource.MINERALS, 15));
		ai.sleep(0.1);
		ai.leftClick(getArmyUnit(10));
		if(num == 1){ai.rightClick(EXTRACTOR);}
		else {ai.rightClick(EXTRACTOR2);}
		ai.type(KeyEvent.VK_B);
		ai.waitFor(min25);
		ai.type(KeyEvent.VK_E);
		ai.sleep(0.1);if(num == 1){ai.leftClick(EXTRACTOR);}
		else {ai.leftClick(EXTRACTOR2);}
	}
	
	private static void buildSpawningPool(AI ai){
		ai.selectAll();
		ai.waitFor(new Resource(Resource.MINERALS, 175));
		ai.sleep(0.1);
		ai.leftClick(getArmyUnit(4));
		ai.rightClick(SPAWNINGPOOL);
		ai.type(KeyEvent.VK_B);
		ai.waitFor(min200);
		ai.type(KeyEvent.VK_S);
		ai.sleep(0.1);
		ai.leftClick(SPAWNINGPOOL);
	}
	
	private static double realToGameTime(double time){
		return time * GAME_SPEED;
	}
	
	private static Point getArmyUnit(int num){
		num--;
		return new Point(490 + 40 *(num % 8), 650 + 40 *(num / 8) );
	}

}
