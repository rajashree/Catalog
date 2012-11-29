/**
 * 
 */
package com.dell.acs.stats.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dell.acs.stats.CountMinMaxStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatUtil;
import com.dell.acs.stats.TimerStat;
import com.dell.acs.stats.TimerStatMutator;
import com.sourcen.dataimport.service.support.AbstractDataImportService;
import com.sourcen.dataimport.service.support.DataImportListener;
import com.sourcen.dataimport.service.support.GenericDataImportService;

/**
 * @author Shawn_Fisk
 *
 */
public class StatGenericsDataImportServiceWrapper implements DataImportListener {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
	private GenericDataImportService dataImportService;
	private ThreadLocal<TimerStatMutator> _timer = new ThreadLocal<TimerStatMutator>();
	
	/**
	 * 
	 */
	public StatGenericsDataImportServiceWrapper(GenericDataImportService pDataImportService) {
		this.dataImportService = pDataImportService;
	}
	
	public void run() {
		this.dataImportService.setListener(this);
		this.dataImportService.run();
		this.dataImportService.setListener(null);
	}

	@Override
	public void beginRun(AbstractDataImportService dataImportService) {
	}

	@Override
	public void endRun(AbstractDataImportService dataImportService) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startLine(AbstractDataImportService dataImportService,
			int recordIndex) {
		String statBaseName = generateBaseName(dataImportService);
		CountStatMutator csm = (CountStatMutator)StatUtil.getInstance().getStat(CountMinMaxStat.class, statBaseName + ".Count");
		TimerStatMutator tsm = (TimerStatMutator)StatUtil.getInstance().getStat(TimerStat.class, statBaseName + ".Timer");
		csm.inc();
		csm.apply();
		this._timer.set(tsm);
		
		tsm.start();
	}

	private String generateBaseName(AbstractDataImportService dataImportService) {
		return "DataImportDetails." + dataImportService.getSourceName();
	}

	@Override
	public void endLine(AbstractDataImportService dataImportService,
			int recordIndex) {
		TimerStatMutator tsm = this._timer.get();
		if (tsm != null) {
			tsm.stop();
			tsm.apply();
		}
		this._timer.set(null);
	}

	@Override
	public void failureLine(AbstractDataImportService dataImportService,
			int recordIndex, Exception e) {
		String statBaseName = generateBaseName(dataImportService);
		CountStatMutator csm = (CountStatMutator)StatUtil.getInstance().getStat(CountMinMaxStat.class, statBaseName + ".FailedCount");
		csm.inc();
		csm.apply();
	}

	@Override
	public void batchFailed(AbstractDataImportService dataImportService,
			String batchId, Integer batchSize) {
		String statBaseName = generateBaseName(dataImportService);
		CountStatMutator csm = (CountStatMutator)StatUtil.getInstance().getStat(CountMinMaxStat.class, statBaseName + ".FailedCount");
		csm.incBy(batchSize);
		csm.apply();
	}
}

