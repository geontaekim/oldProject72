package kr.co.seoulit.logistics.logiinfosvc.hr.to;

public class MenuTO {
	String menuCode, parentMenuCode, menuName, menuLevel, menuURL, menuStatus, childMenu, navMenu, navMenuName;

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getParentMenuCode() {
		return parentMenuCode;
	}

	public void setParentMenuCode(String parentMenuCode) {
		this.parentMenuCode = parentMenuCode;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(String menuLevel) {
		this.menuLevel = menuLevel;
	}

	public String getMenuURL() {
		return menuURL;
	}

	public void setMenuURL(String menuURL) {
		this.menuURL = menuURL;
	}

	public String getMenuStatus() {
		return menuStatus;
	}

	public void setMenuStatus(String menuStatus) {
		this.menuStatus = menuStatus;
	}

	public String getChildMenu() {
		return childMenu;
	}

	public void setChildMenu(String childMenu) {
		this.childMenu = childMenu;
	}

	public String getNavMenu() {
		return navMenu;
	}

	public void setNavMenu(String navMenu) {
		this.navMenu = navMenu;
	}

	public String getNavMenuName() {
		return navMenuName;
	}

	public void setNavMenuName(String navMenuName) {
		this.navMenuName = navMenuName;
	}
	
}
