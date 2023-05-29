package jp.co.internous.valhalla.model.form;

import java.io.Serializable;

/**
 * カートフォーム
 * @author インターノウス
 *
 */
public class CartForm implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int productId;
	private int productCount;
	private int userId;

	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getProductCount() {
		return productCount;
	}
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
