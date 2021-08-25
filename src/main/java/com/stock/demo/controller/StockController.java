package com.stock.demo.controller;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stock.demo.entities.StockAlerts;
import com.stock.demo.entities.Users;
import com.stock.demo.service.StockAlertService;
import com.stock.demo.service.UserService;
import com.stock.demo.utilities.StockConstants;

@Controller
@SessionAttributes("user")
@RequestMapping("/saved")
public class StockController {

	@Autowired
	StockAlertService stockAlertService;
	
	@Autowired
	UserService userService;
	
	Users user;

	@PostConstruct
	public void populateUser() {
		
		user = userService.findById(1l);
	}
	
	@GetMapping("/view")
	public String viewStocks(ModelMap model) {
		
		model.addAttribute("user", user);	//for demo purpose
		List<StockAlerts> allStocks = stockAlertService.findByUserId(user.getId());
		Collections.sort(allStocks, StockAlerts.alertDiffAsc);
		model.addAttribute("stockAlertList", allStocks);
		return "/stock/view";
	}

	@GetMapping("/add")
	public String viewAddStock(@ModelAttribute(StockConstants.STOCK_AlERTS) StockAlerts alerts, ModelMap model) {

//		model.clear();
		return "/stock/add";
	}

	@PostMapping("/add")
	public String addStock(@ModelAttribute(StockConstants.STOCK_AlERTS) @Valid StockAlerts alerts, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute(StockConstants.STOCK_AlERTS, alerts);
			return "/stock/add";
		}
		boolean isNewAlert = true;
		if(alerts.getId() != null) {
			isNewAlert = false;
		}
		StockAlerts saved = stockAlertService.save(alerts);
		String successMsg = "<strong>"+saved.getStock().getCompanyName()+"</strong>";
		if(isNewAlert) {
			successMsg += " has been Added to Alerts";
		} else {
			successMsg += " has been Updated";
		}
		redirectAttributes.addFlashAttribute("message", successMsg);
		redirectAttributes.addFlashAttribute("messageCss", "alert alert-success alert-dismissible fade show");
		return "redirect:view";
	}

	@GetMapping("/update")
	public String updateStock(@RequestParam("id") Long id, ModelMap model) {
		StockAlerts findById = stockAlertService.findById(id);
		model.addAttribute(StockConstants.STOCK_AlERTS, findById);
		return "/stock/add";
	}

	@GetMapping("/delete")
	public String deleteStock(@RequestParam("id") Long id,
			@RequestParam(value = "symbol", required = false) String symbol, ModelMap model,
			RedirectAttributes redirectAttributes) {
		
		stockAlertService.deleteById(id);
		redirectAttributes.addFlashAttribute("message", "<strong>"+symbol+"</strong>" + "   Removed from alerts!!");
		redirectAttributes.addFlashAttribute("messageCss", "alert alert-warning alert-dismissible fade show");
		return "redirect:view";
	}

}
