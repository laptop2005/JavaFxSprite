package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

class CharacterSprite {
	//움직임을 표시하는 이미지 파트가 들어있는 이미지 파일경로
    private static final String SPRITE_SHEET_PATH = "sprite.gif";
    
    //움직임을 표시할 단계 - 캐릭터의 움직임이 네가지로 끊어져서 구분됨
    private static final int MAX_MOVING_INDEX = 4;
    
    //캐릭터가 한번 움직일때 이동할 픽셀 수
    private static final int DELTA = 4;
    
    //키보드 누름에 따른 캐릭터의 방향 KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT 
    private KeyCode direction;
    
    //캐릭터가 서있을 때 표시할 이미지 맵
    private Map<KeyCode, SpriteVO> standingImgMap = new EnumMap<>(KeyCode.class);
    
    //캐릭터가 움직일 때 표시할 이미지 맵 - 움직일때는 계속해서 이미지가 바뀌므로 애니메이션 효과를 위한 이미지가 여러장 들어간다
    private Map<KeyCode, List<SpriteVO>> movingImgMap = new EnumMap<>(KeyCode.class);
    
    //캐릭터의 위치 x
    private double x;
    
    //캐릭터의 위치 y
    private double y;
    
    //캐릭터의 최대이동가능x
    private double maxX;
    
    //캐릭터의 최대이동가능y
    private double maxY;
    
    //캐릭터가 서있는지 움직이는지 여부
    private boolean moving = false;
    
    //캐릭터가 움직일때 몇번째 움직임 모양인지 기억하기 위한 변수
    private int movingIndex = 0;
    
    //캐릭터 이미지 객체
    private Image img;

    //생성자 - 최초에 캐릭터가 서있을 방향과 위치를 정해 줌
    public CharacterSprite(KeyCode direction, double x, double y, double maxX, double maxY) throws IOException {
        this.direction = direction;
        this.x = x;
        this.y = y;
        this.maxX = maxX;
        this.maxY = maxY;
        createSprites();
    }
    
    //캐릭터의 행동 마다의 이미지를 어느 부분에서 떼어와야 하는지 계산해서 맵에 넣어주는 메서드
    //캐릭터의 행동이미지를 따로 분리해서 연속으로 바꿔주면 캐릭터가 움직이는 것처럼 보이는데
    //이걸 스프라이트 라고 한다.
    private void createSprites() throws IOException {
    	
    	//이미지 경로로부터 이미지를 불러와서 객체로 만들어준다.
    	img = new Image(getClass().getResourceAsStream(SPRITE_SHEET_PATH));

        // 캐릭터의 첫번째 움직임 이미지 좌표
        int x0 = 0;
        int y0 = 64;
        int rW = 32;
        int rH = 32;
        
        //움직이는 방향별로 보여줘야 할 이미지가 다르기 때문에 방향에 따라 이미지를 나누기 쉽게 하기위해 
        //방향을 배열로 만들어 줌
        KeyCode[] arrKeyCode = {KeyCode.DOWN,KeyCode.LEFT,KeyCode.UP,KeyCode.RIGHT};
        
        //반복문으로 방향에 따른 애니메이션 이미지 구역을 나누어 좌표를 저장
        for (int row=0;row<arrKeyCode.length;row++) {
        	
        	//방향키에 따른 캐릭터의 방향
            KeyCode dir = arrKeyCode[row];
            
            List<SpriteVO> imgList = new ArrayList<>();
            movingImgMap.put(dir, imgList);
            
            int rY = y0 + row * rH;
            
            //이미지 스프라이트가 서있는이미지, 움직임1, 움직임2, 움직임3, 움직임4, 움직임5 순으로 있음
            for (int col = 0; col < 5; col++) {
                int rX = x0 + col * rW;
                
                SpriteVO vo = new SpriteVO(rX, rY, rW, rH);
                
//                System.out.println("row:"+row+",rx:"+rX+",ry:"+rY+",rw:"+rW+",rh:"+rH);
                
                if (col == 0) {
                    // 첫번째 이미지는 서있는 이미지
                    standingImgMap.put(dir, vo);
                } else {
                    // 나머지 다른 이미지들은 모두 움직이는 이미지
                    imgList.add(vo);
                }
            }
        }
    }

    //캐릭터 이미지를 캔버스에 그려줌
    public void draw(GraphicsContext g) {
        SpriteVO vo = null;
        
        //움직이고 있지 않으면
        if (!moving) {
        	//서있는 이미지를 표시
            vo = standingImgMap.get(direction);
        } else {
        	//움직이고 있으면 움직이는 모양에 맞는 이미지를 표시
            vo = movingImgMap.get(direction).get(movingIndex);
        }
        
        //drawImage(이미지원본, 원본x좌표,원본y좌표,원본넓이, 원본높이, 캔버스x좌표, 캔버스y좌표, 캔버스에표시할넓이, 캔버스에표시할높이)
        //이미지 원본에서 dx,dy 에 있는 이미지를 dw,dh만큼 떼어와 캔버스의 x,y 좌표에 dw,dh만큼 그림
		g.drawImage(img, vo.getDx(), vo.getDy(), vo.getDw(), vo.getDh(), x, y, vo.getDw(), vo.getDh());
    }

    //캐릭터의 방향을 설정
    public void setDirection(KeyCode direction) {
    	//만약 방향이 달라졌다면 움직임을 멈춤으로 바꿈
        if (this.direction != direction) {
            setMoving(false);
        }
        this.direction = direction;

    }

    //움직임 여부 설정
    public void setMoving(boolean moving) {
        this.moving = moving;
        if (!moving) {
            movingIndex = 0;
        }
    }

    //키보드를 한번 누를 때마다 캐릭터의 움직임을 계산해 줌
    public void tick() {
        if (moving) {
        	if(direction==KeyCode.RIGHT){
        		x += DELTA;
        	}else if(direction==KeyCode.LEFT){
        		x -= DELTA;
        	}else if(direction==KeyCode.DOWN){
        		y += DELTA;
        	}else if(direction==KeyCode.UP){
        		y -= DELTA;
        	}
        	
        	//캐릭터가 움직일 수 있는 범위를 벗어났다면 더이상 가지 않도록
        	if(x<0||x>maxX-32||y<0||y>maxY-32){
        		if(direction==KeyCode.RIGHT){
	            	x -= DELTA;
	            }else if(direction==KeyCode.LEFT){
	            	x += DELTA;
	            }else if(direction==KeyCode.DOWN){
	            	y -= DELTA;
	            }else if(direction==KeyCode.UP){
	            	y += DELTA;
	            }
        	}
            movingIndex++;
            movingIndex %= MAX_MOVING_INDEX;
        }
    }
}