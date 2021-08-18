package com.stock.demo.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stock.demo.entities.Stock;
import com.stock.demo.exception.StockNotFoundException;
import com.stock.demo.repo.StockRepository;
import com.stock.demo.service.StockService;

@Service
public class StockServiceImpl implements StockService {

	@Autowired
	StockRepository stockRepo;

	@Override
	public Stock save(Stock stock) {
		try {
			return stockRepo.save(stock);
		} catch (Exception e) {
			System.out.println("Error saving stock with symbol - "+stock.getSymbol()+" : "+e);
		}
		return null;
	}

	@Override
	public List<Stock> saveAll(List<Stock> stock) {
		try {
			return stockRepo.saveAll(stock);
		} catch (Exception e) {
			System.out.println("Error saving stock list of stocks - "+e);
		}
		return null;
	}

	@Override
	public List<Stock> findAll() {
		return stockRepo.findAll();
	}

	@Override
	public Stock findBySymbol(String symbol) {
		
		return stockRepo.findBySymbol(symbol);
	}

	@Override
	public void deleteAll() {
		stockRepo.deleteAll();
	}

	@Override
	public void deleteBySymbol(String symbol) {
		// TODO
	}

	@Override
	public void deleteById(Long id) {
		try {
			stockRepo.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Stock findById(Long id) {
		return stockRepo.findById(id).orElseThrow(() -> new StockNotFoundException(String.valueOf(id)));
	}

}
