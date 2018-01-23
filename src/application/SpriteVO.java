package application;

public class SpriteVO {
	private double dx, dy, dw, dh;
	
	public SpriteVO(double dx, double dy, double dw, double dh){
		this.dx = dx;
		this.dy = dy;
		this.dw = dw;
		this.dh = dh;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public double getDw() {
		return dw;
	}

	public void setDw(double dw) {
		this.dw = dw;
	}

	public double getDh() {
		return dh;
	}

	public void setDh(double dh) {
		this.dh = dh;
	}
	
}