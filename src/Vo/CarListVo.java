package Vo;


//VO역할을 하는 조회한 차량 한대의 정보를 저장할 용도 또는
//차량 정보 한대의 정보를 DB의 CarList테이블에 추가할 용도의 클래스 
public class CarListVo {
	
	private int carno;//차 번호
	private String carname; //차량 명 
	private String carcompany;//차 제조사명 (현대,기아 등)
	private int carprice;//차 한대 당 렌트 가격 
	private int carusepepople; //차량 인승정보 (4인승 ,6인승, 9인승 등)
	private String carinfo; //차량 상세 정보 
	private String carimg; //차량 이미지 명
	private String carcategory; //차 유형( 소형:Small, 중형:Mid, 대형:Big)
	
	public CarListVo() {} //기본생성자

	//CarListVo클래스의 객체 생성시 모든 인스턴스변수값 초기화 할 생성자
	public CarListVo(int carno, String carname, 
					 String carcompany, int carprice, 
					 int carusepepople, String carinfo,
					 String carimg, String carcategory) {
		super();
		this.carno = carno;
		this.carname = carname;
		this.carcompany = carcompany;
		this.carprice = carprice;
		this.carusepepople = carusepepople;
		this.carinfo = carinfo;
		this.carimg = carimg;
		this.carcategory = carcategory;
	}
	
	//getter,setter

	public int getCarno() {
		return carno;
	}

	public void setCarno(int carno) {
		this.carno = carno;
	}

	public String getCarname() {
		return carname;
	}

	public void setCarname(String carname) {
		this.carname = carname;
	}

	public String getCarcompany() {
		return carcompany;
	}

	public void setCarcompany(String carcompany) {
		this.carcompany = carcompany;
	}

	public int getCarprice() {
		return carprice;
	}

	public void setCarprice(int carprice) {
		this.carprice = carprice;
	}

	public int getCarusepepople() {
		return carusepepople;
	}

	public void setCarusepepople(int carusepepople) {
		this.carusepepople = carusepepople;
	}

	public String getCarinfo() {
		return carinfo;
	}

	public void setCarinfo(String carinfo) {
		this.carinfo = carinfo;
	}

	public String getCarimg() {
		return carimg;
	}

	public void setCarimg(String carimg) {
		this.carimg = carimg;
	}

	public String getCarcategory() {
		return carcategory;
	}

	public void setCarcategory(String carcategory) {
		this.carcategory = carcategory;
	}
	
	
	

}









