package com.stock.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stock.demo.entities.StockAlerts;

public interface StockAlertsRepository extends JpaRepository<StockAlerts, Long> {

	List<StockAlerts> findByUserId(Long id);
	
	List<StockAlerts> findByStockId(Long stockId);

	List<StockAlerts> findByStockIdAndIsAlertEnabledAndIsMailSend(Long stockId, boolean isAlertEnabled,
			boolean isMailSend);
	
	@Query("from StockAlerts where stock.id=:stockId and (highThan52=:high52 and highVolume=:highVol"
			+ " and higherAvgVolume=:highAvgVol and pChangeCrossed=:pChange)")
	List<StockAlerts> findByStockId_HighVolumeOrHigherAvgVolumeOrPChangeCrossed(Long stockId,boolean high52,
			boolean highVol, boolean highAvgVol, boolean pChange);
}
