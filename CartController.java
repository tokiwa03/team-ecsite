package jp.co.internous.valhalla.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.valhalla.model.domain.TblCart;
import jp.co.internous.valhalla.model.domain.dto.CartDto;
import jp.co.internous.valhalla.model.form.CartForm;
import jp.co.internous.valhalla.model.mapper.TblCartMapper;
import jp.co.internous.valhalla.model.session.LoginSession;


/**
 * カート情報に関する処理のコントローラー
 * @author インターノウス
 *
 */
@Controller
@RequestMapping("/valhalla/cart")
public class CartController {
	
	@Autowired
	private TblCartMapper cartMapper;
	
	@Autowired
	private LoginSession loginSession;

	private Gson gson = new Gson();
	

	/**
	 * カート画面を初期表示する。
	 * @param m 画面表示用オブジェクト
	 * @return カート画面
	 */
	@RequestMapping("/")
	public String index(Model m) {
	    int userId = loginSession.getUserId(); // ユーザーIDを取得
		
		List<CartDto> carts = cartMapper.findByUserId(userId);// カート情報を取得

		m.addAttribute("loginSession", loginSession);
		m.addAttribute("carts", carts);
		
		return "cart";
	}
	
	/**
	 * カートに追加処理を行う
	 * @param f カート情報のForm
	 * @param m 画面表示用オブジェクト
	 * @return カート画面
	 */
	@RequestMapping("/add")
	public String addCart(CartForm f, Model m) {
	    int userId = loginSession.getUserId(); // ユーザーIDを取得

	    List<CartDto> carts = cartMapper.findByUserId(userId); // ユーザーIDを条件にカート情報を取得

	    CartDto existingCart = null;
	    for (CartDto cart : carts) {
	        if (cart.getProductId() == f.getProductId()) { // カート情報が存在する場合は数量を更新する
	            int newProductCount = cart.getProductCount() + f.getProductCount();
	            cart.setProductCount(newProductCount);
	            TblCart tblCart = new TblCart();
	            tblCart.setId(cart.getId());
	            tblCart.setUserId(cart.getUserId());
	            tblCart.setProductId(cart.getProductId());
	            tblCart.setProductCount(cart.getProductCount());
	            cartMapper.update(tblCart);
	            existingCart = cart;
	            break;
	        }
	    }

	    if (existingCart == null) {
	        // カート情報が存在しない場合は新規登録する
	        TblCart newCart = new TblCart();
	        newCart.setUserId(userId);
	        newCart.setProductId(f.getProductId());
	        newCart.setProductCount(f.getProductCount());
	        cartMapper.insert(newCart);
	    }
	    carts = cartMapper.findByUserId(userId);
	    m.addAttribute("loginSession", loginSession);
	    m.addAttribute("carts", carts); 
	    return "cart";
	}

	/**
	 * カート情報を削除する
	 * @param checkedIdList 選択したカート情報のIDリスト
	 * @return true:削除成功、false:削除失敗
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/delete")
	@ResponseBody
	public boolean deleteCart(@RequestBody String checkedIdList) {
		int deleteCount;
		
		Map<String, List<Integer>> cartIdList = gson.fromJson(checkedIdList, Map.class);
		List<Integer> checkedIds = cartIdList.get("checkedIdList");
		
		deleteCount = cartMapper.deleteById(checkedIds);
	    return deleteCount == cartIdList.size();
	}
}
