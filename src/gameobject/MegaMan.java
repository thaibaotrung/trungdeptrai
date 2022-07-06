		package gameobject;

import state.GameWorldState;
import effect.Animation;
import effect.AnimationHandler;
import effect.CacheDataLoader;
import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MegaMan extends Human {

    public static final int RUNSPEED = 3;// tốc độ của megaman = 3;
    
//    private Animation runForwardAnim, runBackAnim, runShootingForwarAnim, runShootingBackAnim;
//    private Animation idleForwardAnim, idleBackAnim, idleShootingForwardAnim, idleShootingBackAnim;
//    private Animation dickForwardAnim, dickBackAnim;
//    private Animation flyForwardAnim, flyBackAnim, flyShootingForwardAnim, flyShootingBackAnim;
//    private Animation landingForwardAnim, landingBackAnim;
//    
//    private Animation climWallForward, climWallBack;
    public String name="megaMan";
    private long lastShootingTime;//thời gian cuối cùng megaman bắn
    private boolean isShooting = false;//trạng thái bắn
    private AudioClip hurtingSound;
    private AudioClip shooting1;
    public AnimationHandler animationH = new AnimationHandler(name);
    
    public MegaMan(float x, float y, GameWorldState gameWorld) {
        super(x, y, 70, 90, 0.1f, 100, gameWorld); 
        shooting1 = CacheDataLoader.getInstance().getSound("bluefireshooting");
        hurtingSound = CacheDataLoader.getInstance().getSound("megamanhurt");
        
        setTeamType(LEAGUE_TEAM);

        setTimeForNoBehurt(2000*1000000);
        
//        runForwardAnim = CacheDataLoader.getInstance().getAnimation("run");
//        runBackAnim = CacheDataLoader.getInstance().getAnimation("run");
//        runBackAnim.flipAllImage();   
//        
//        idleForwardAnim = CacheDataLoader.getInstance().getAnimation("idle");
//        idleBackAnim = CacheDataLoader.getInstance().getAnimation("idle");
//        idleBackAnim.flipAllImage();
//        
//        dickForwardAnim = CacheDataLoader.getInstance().getAnimation("dick");
//        dickBackAnim = CacheDataLoader.getInstance().getAnimation("dick");
//        dickBackAnim.flipAllImage();
//        
//        flyForwardAnim = CacheDataLoader.getInstance().getAnimation("flyingup");
//        flyForwardAnim.setIsRepeated(false);
//        flyBackAnim = CacheDataLoader.getInstance().getAnimation("flyingup");
//        flyBackAnim.setIsRepeated(false);
//        flyBackAnim.flipAllImage();
//        
//        landingForwardAnim = CacheDataLoader.getInstance().getAnimation("landing");
//        landingBackAnim = CacheDataLoader.getInstance().getAnimation("landing");
//        landingBackAnim.flipAllImage();
//        
//        climWallBack = CacheDataLoader.getInstance().getAnimation("clim_wall");
//        climWallForward = CacheDataLoader.getInstance().getAnimation("clim_wall");
//        climWallForward.flipAllImage();
//        
//        behurtForwardAnim = CacheDataLoader.getInstance().getAnimation("hurting");
//        behurtBackAnim = CacheDataLoader.getInstance().getAnimation("hurting");
//        behurtBackAnim.flipAllImage();
//        
//        idleShootingForwardAnim = CacheDataLoader.getInstance().getAnimation("idleshoot");
//        idleShootingBackAnim = CacheDataLoader.getInstance().getAnimation("idleshoot");
//        idleShootingBackAnim.flipAllImage();
//        
//        runShootingForwarAnim = CacheDataLoader.getInstance().getAnimation("runshoot");
//        runShootingBackAnim = CacheDataLoader.getInstance().getAnimation("runshoot");
//        runShootingBackAnim.flipAllImage();
//        
//        flyShootingForwardAnim = CacheDataLoader.getInstance().getAnimation("flyingupshoot");
//        flyShootingBackAnim = CacheDataLoader.getInstance().getAnimation("flyingupshoot");
//        flyShootingBackAnim.flipAllImage();
        
    }
    //ghi đè phương thức update từ lớp cha
    @Override
    public void Update() {

        super.Update();
        
        if(isShooting){
            if(System.nanoTime() - lastShootingTime > 500*1000000){
                isShooting = false;
            }
        }
        
        if(getIsLanding()){
            animationH.landingBackAnim.Update(System.nanoTime());
            if(animationH.landingBackAnim.isLastFrame()) {
                setIsLanding(false);
                animationH.landingBackAnim.reset();
                animationH.runForwardAnim.reset();
                animationH.runBackAnim.reset();
            }
        }
        
    }
    //ghi đè phương thức từ lớp cha
    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        // TODO Auto-generated method stub
        Rectangle rect = getBoundForCollisionWithMap();
        
        if(getIsDicking()){
            rect.x = (int) getPosX() - 22;
            rect.y = (int) getPosY() - 20;
            rect.width = 44;
            rect.height = 65;
        }else{
            rect.x = (int) getPosX() - 22;
            rect.y = (int) getPosY() - 40;
            rect.width = 44;
            rect.height = 80;
        }
        
        return rect;
    }

    //phương thức vẽ animation tương ứng với các trạng thái của megamnan
    @Override
    public void draw(Graphics2D g2) {
        
        switch(getState()){
        
            case ALIVE:
            case NOBEHURT:
                if(getState() == NOBEHURT && (System.nanoTime()/10000000)%2!=1)
                {
                    System.out.println("Plash...");
                }else{
                    
                    if(getIsLanding()){

                        if(getDirection() == RIGHT_DIR){
                        	animationH.landingForwardAnim.setCurrentFrame(animationH.landingBackAnim.getCurrentFrame());
                        	animationH.landingForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                                    (int) getPosY() - (int) getGameWorld().camera.getPosY() + (getBoundForCollisionWithMap().height/2 - animationH.landingForwardAnim.getCurrentImage().getHeight()/2),
                                    g2);
                        }else{
                        	animationH.landingBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                                    (int) getPosY() - (int) getGameWorld().camera.getPosY() + (getBoundForCollisionWithMap().height/2 - animationH.landingBackAnim.getCurrentImage().getHeight()/2),
                                    g2);
                        }

                    }else if(getIsJumping()){

                        if(getDirection() == RIGHT_DIR){
                        	animationH.flyForwardAnim.Update(System.nanoTime());
                            if(isShooting){
                            	animationH.flyShootingForwardAnim.setCurrentFrame(animationH.flyForwardAnim.getCurrentFrame());
                            	animationH.flyShootingForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()) + 10, (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            }else
                            	animationH.flyForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                        }else{
                        	animationH.flyBackAnim.Update(System.nanoTime());
                            if(isShooting){
                            	animationH.flyShootingBackAnim.setCurrentFrame(animationH.flyBackAnim.getCurrentFrame());
                            	animationH.flyShootingBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()) - 10, (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            }else
                            	animationH.flyBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                        }

                    }else if(getIsDicking()){

                        if(getDirection() == RIGHT_DIR){
                        	animationH.dickForwardAnim.Update(System.nanoTime());
                        	animationH.dickForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                                    (int) getPosY() - (int) getGameWorld().camera.getPosY() + (getBoundForCollisionWithMap().height/2 - animationH.dickForwardAnim.getCurrentImage().getHeight()/2),
                                    g2);
                        }else{
                        	animationH.dickBackAnim.Update(System.nanoTime());
                        	animationH.dickBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                                    (int) getPosY() - (int) getGameWorld().camera.getPosY() + (getBoundForCollisionWithMap().height/2 - animationH.dickBackAnim.getCurrentImage().getHeight()/2),
                                    g2);
                        }

                    }else{
                        if(getSpeedX() > 0){
                        	animationH.runForwardAnim.Update(System.nanoTime());
                            if(isShooting){
                            	animationH.runShootingForwarAnim.setCurrentFrame(animationH.runForwardAnim.getCurrentFrame() - 1);
                            	animationH.runShootingForwarAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            }else
                            	animationH.runForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            if(animationH.runForwardAnim.getCurrentFrame() == 1) animationH.runForwardAnim.setIgnoreFrame(0);
                        }else if(getSpeedX() < 0){
                        	animationH.runBackAnim.Update(System.nanoTime());
                            if(isShooting){
                            	animationH.runShootingBackAnim.setCurrentFrame(animationH.runBackAnim.getCurrentFrame() - 1);
                            	animationH.runShootingBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            }else
                            	animationH.runBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            if(animationH.runBackAnim.getCurrentFrame() == 1) animationH.runBackAnim.setIgnoreFrame(0);
                        }else{
                            if(getDirection() == RIGHT_DIR){
                                if(isShooting){
                                	animationH.idleShootingForwardAnim.Update(System.nanoTime());
                                	animationH.idleShootingForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                                }else{
                                	animationH.idleForwardAnim.Update(System.nanoTime());
                                	animationH.idleForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                                }
                            }else{
                                if(isShooting){
                                	animationH.idleShootingBackAnim.Update(System.nanoTime());
                                	animationH.idleShootingBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                                }else{
                                	animationH.idleBackAnim.Update(System.nanoTime());
                                	animationH.idleBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                                }
                            }
                        }            
                    }
                }
                
                break;
            
            case BEHURT:
                if(getDirection() == RIGHT_DIR){
                    animationH.behurtForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                }else{
                    animationH.behurtBackAnim.setCurrentFrame(animationH.behurtForwardAnim.getCurrentFrame());
                    animationH.behurtBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                }
                break;
             
            case FEY:
                
                break;

        }
        
        //drawBoundForCollisionWithMap(g2);
        //drawBoundForCollisionWithEnemy(g2);
    }
    
    //phương thức chạy của megaman
    @Override
    public void run() {
        if(getDirection() == LEFT_DIR)
            setSpeedX(-3);
        else setSpeedX(3);
    }
    
    //phương thức nhảy của megaman
    @Override
    public void jump() {

        if(!getIsJumping()){
            setIsJumping(true);
            setSpeedY(-5.0f);           
            animationH.flyBackAnim.reset();
            animationH.flyForwardAnim.reset();
        }
        // for clim wall
        else{
            Rectangle rectRightWall = getBoundForCollisionWithMap();
            rectRightWall.x += 1;
            Rectangle rectLeftWall = getBoundForCollisionWithMap();
            rectLeftWall.x -= 1;
            
            if(getGameWorld().physicalMap.haveCollisionWithRightWall(rectRightWall)!=null && getSpeedX() > 0){
                setSpeedY(-5.0f);
                //setSpeedX(-1);
                animationH.flyBackAnim.reset();
                animationH.flyForwardAnim.reset();
                //setDirection(LEFT_DIR);
            }else if(getGameWorld().physicalMap.haveCollisionWithLeftWall(rectLeftWall)!=null && getSpeedX() < 0){
                setSpeedY(-5.0f);
                //setSpeedX(1);
                animationH.flyBackAnim.reset();
                animationH.flyForwardAnim.reset();
                //setDirection(RIGHT_DIR);
            }
                
        }
    }
    
    //phương thức ngồi của megaman
    @Override
    public void dick() {
        if(!getIsJumping())
            setIsDicking(true);
    }
    
    //phương thức đứng dậy của megaman
    @Override
    public void standUp() {
        setIsDicking(false);
        animationH.idleForwardAnim.reset();
        animationH.idleBackAnim.reset();
        animationH.dickForwardAnim.reset();
        animationH.dickBackAnim.reset();
    }
    
    //phương thức dừng chạy của megaman
    @Override
    public void stopRun() {
        setSpeedX(0);
        animationH.runForwardAnim.reset();
        animationH.runBackAnim.reset();
        animationH.runForwardAnim.unIgnoreFrame(0);
        animationH.runBackAnim.unIgnoreFrame(0);
    }
    
    //phương thức tấn công của megaman
    @Override
    public void attack() {
    
        if(!isShooting && !getIsDicking()){
            
            shooting1.play();
            
            Bullet bullet = new BlueFire(getPosX(), getPosY(), getGameWorld());
            if(getDirection() == LEFT_DIR) {
                bullet.setSpeedX(-10);
                bullet.setPosX(bullet.getPosX() - 40);
                if(getSpeedX() != 0 && getSpeedY() == 0){
                    bullet.setPosX(bullet.getPosX() - 10);
                    bullet.setPosY(bullet.getPosY() - 5);
                }
            }else {
                bullet.setSpeedX(10);
                bullet.setPosX(bullet.getPosX() + 40);
                if(getSpeedX() != 0 && getSpeedY() == 0){
                    bullet.setPosX(bullet.getPosX() + 10);
                    bullet.setPosY(bullet.getPosY() - 5);
                }
            }
            if(getIsJumping())
                bullet.setPosY(bullet.getPosY() - 20);
            
            bullet.setTeamType(getTeamType());
            getGameWorld().bulletManager.addObject(bullet);
            
            lastShootingTime = System.nanoTime();
            isShooting = true;
            
        }
    
    }
    @Override
    public void hurtingCallback(){
        System.out.println("Call back hurting");
        hurtingSound.play();
    }

}
