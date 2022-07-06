package gameobject;

import state.GameWorldState;
import java.awt.Rectangle;

public abstract class Human extends ParticularObject{

    private boolean isJumping;//dang nhay
    private boolean isDicking;//dang ngoi
    
    private boolean isLanding;//dang tiep dat

    public Human(float x, float y, float width, float height, float mass, int blood, GameWorldState gameWorld) {
        super(x, y, width, height, mass, blood, gameWorld);
        setState(ALIVE);
    }

    public abstract void run();//lớp trừu tượng chạy
   
    public abstract void jump();//lớp trừu tượng nhảy
    
    public abstract void dick();//lớp trừu tượng ngồi
    
    public abstract void standUp();//lớp trừu tượng đúng dậy
    
    public abstract void stopRun();//lớp trừu tượng dừng chạy

    public boolean getIsJumping() {
        return isJumping;
    }
    
    public void setIsLanding(boolean b){
        isLanding = b;
    }
    
    public boolean getIsLanding(){
        return isLanding;
    }
    
    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public boolean getIsDicking() {
        return isDicking;
    }

    public void setIsDicking(boolean isDicking) {
        this.isDicking = isDicking;
    }
    
    //phương thức update các trạng thái của nhân vật 
    @Override
    public void Update(){
        
        super.Update();
        //trang thai nhan vat dang song hoac khong bi dau
        if(getState() == ALIVE || getState() == NOBEHURT){
        //trang thai ko phai trang thai tiep dat
            if(!isLanding){

                setPosX(getPosX() + getSpeedX());

                //kiem tra va cham voi map ben trai
                if(getDirection() == LEFT_DIR && 
                        getGameWorld().physicalMap.haveCollisionWithLeftWall(getBoundForCollisionWithMap())!=null){

                    Rectangle rectLeftWall = getGameWorld().physicalMap.haveCollisionWithLeftWall(getBoundForCollisionWithMap());
                    setPosX(rectLeftWall.x + rectLeftWall.width + getWidth()/2);//set lai vi tri cho dung

                }
                if(getDirection() == RIGHT_DIR && 
                        getGameWorld().physicalMap.haveCollisionWithRightWall(getBoundForCollisionWithMap())!=null){

                    Rectangle rectRightWall = getGameWorld().physicalMap.haveCollisionWithRightWall(getBoundForCollisionWithMap());
                    setPosX(rectRightWall.x - getWidth()/2);

                }



                /**
                 * Codes below check the posY of megaMan
                 */
                // plus (+2) because we must check below the character when he's speedY = 0
                //set vi tri theo chieu y
                Rectangle boundForCollisionWithMapFuture = getBoundForCollisionWithMap();
                boundForCollisionWithMapFuture.y += (getSpeedY()!=0?getSpeedY(): 2);
                Rectangle rectLand = getGameWorld().physicalMap.haveCollisionWithLand(boundForCollisionWithMapFuture);//mat dat
                
                Rectangle rectTop = getGameWorld().physicalMap.haveCollisionWithTop(boundForCollisionWithMapFuture);//tren cung
                
                //va chạm với trần nhà
                if(rectTop !=null){
                    
                    setSpeedY(0);
                    setPosY(rectTop.y + getGameWorld().physicalMap.getTileSize() + getHeight()/2);
                    
                }else if(rectLand != null)//va chạm với mặt đất
                {
                    setIsJumping(false);
                    if(getSpeedY() > 0) setIsLanding(true);
                    setSpeedY(0);
                    setPosY(rectLand.y - getHeight()/2 - 1);
                }else{
                    setIsJumping(true);
                    setSpeedY(getSpeedY() + getMass());
                    setPosY(getPosY() + getSpeedY());
                }
            }
        }
    }
    
}
