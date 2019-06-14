package com.heqichao.springBootDemo.module.control;

import com.heqichao.springBootDemo.base.control.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.module.service.ProductsService;

@RestController
@RequestMapping(value = "/service")
public class ProductsController extends BaseController{

	@Autowired
	private ProductsService productsService;
	
	@RequestMapping(value = "/queryProducts")
	ResponeResult queryLiteApps() throws Exception {
		return new ResponeResult(productsService.queryProducts());
	}
	
	@RequestMapping(value = "/editProduct")
	ResponeResult addLiteApp() throws Exception {
		return productsService.editProduct();
	}
	

	@RequestMapping(value = "/deleteProductsById")
	ResponeResult deleteProductsById() throws Exception {
		Integer id =getIntegerParam("eid");
		return productsService.deleteProductsById(id);
	}
	
	@RequestMapping(value = "/getProductsList" )
    public ResponeResult getProductsList() throws Exception {
    	return productsService.getProductsList();
    }
	
}

