/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.dataimport.controller.admin;

import com.dell.acs.content.EntityConstants.EnumDataImport;
import com.dell.acs.managers.DataFileStatisticService;
import com.dell.acs.managers.DataFilesDownloadManager;
import com.dell.acs.managers.DataImportManager.FileStatus;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.DataFileStatistic;
import com.dell.acs.persistence.domain.DataFileStatisticSummary;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.DataFileRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.persistence.repository.UnvalidatedProductRepository;
import com.dell.acs.web.dataimport.controller.BaseDellController;
import com.dell.acs.web.dataimport.model.admin.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
@Controller
public class AdminIndexController extends BaseDellController {
	protected List<DataFileBean> EMPTY_LIST = new ArrayList<DataFileBean>();

	@RequestMapping(value = "admin/index.do", method = RequestMethod.GET)
	public void getIndex(@RequestParam(defaultValue="") String retailerSite, @RequestParam(defaultValue="") String host, @RequestParam(defaultValue="") String openNodes, Model model) {
		model.addAttribute("selectedRetailerSite", retailerSite);
		model.addAttribute("selectedHost", host);
		model.addAttribute("retailerSites",
				retailerManager.getAllRetailerSites());
		model.addAttribute("hosts",
				this.dataFileRepository.getHosts());
		Set<String> openNodeSet = new HashSet<String>();
		for(String nodeIdStr : openNodes.split(",")) {
			openNodeSet.add(nodeIdStr);
		}
		
		try {
			if (retailerSite.compareTo("") != 0) {
				RetailerSite rs = this.retailerSiteRepository.getByName(retailerSite, true);
				if (rs == null) {
					throw new RuntimeException(
							"Invalid retailer site name: "
									+ retailerSite);
				}
				DataFilesDownloadManager dfdm = getRetailerSiteDFDM(rs);
				if (dfdm != null) {
					Collection<DataFileNodeBean> nodes = new ArrayList<DataFileNodeBean>();
					Collection<DataFile> dataFiles = null;
					if (host.compareTo("") == 0) {
						dataFiles = dataFileRepository.getDataFilesByRetailerSite(rs);
					} else {
						dataFiles = dataFileRepository.getDataFilesByRetailerSiteAndHost(rs, host);
					}

                    // Read statistics summary
                    Collection<DataFileStatisticSummary> dataFileStatSummaries = null;
                    dataFileStatSummaries = this.getDataFileStatisticService().getSummaryByRetailerSite(rs);

                    Map<Long, DataStatSummaryBean> summariesByDataFileId = new HashMap<Long, DataStatSummaryBean>();

                    List<Long> skipStatsDataFileIds = new ArrayList<Long>();

                    for(DataFileStatisticSummary dataFileStatSummary : dataFileStatSummaries) {
                        skipStatsDataFileIds.add(dataFileStatSummary.getDataFile_id());
                        DataStatSummaryBean summary = summariesByDataFileId.get(dataFileStatSummary.getDataFile_id());
                        if (summary == null) {
                            summary = new DataStatSummaryBean(dataFileStatSummary);
                            summariesByDataFileId.put(dataFileStatSummary.getDataFile_id(), summary);
                        }
                    }

                    // Read statistics
					Collection<DataFileStatistic> dataFileStats = null;
					if (host.compareTo("") == 0) {
						dataFileStats = this.getDataFileStatisticService().getByRetailerSite(rs, skipStatsDataFileIds);
					} else {
						dataFileStats = this.getDataFileStatisticService().getByRetailerSiteAndHost(rs, host, skipStatsDataFileIds);
					}
					
					for(DataFileStatistic dataFileStat : dataFileStats) {
						DataStatSummaryBean summary = summariesByDataFileId.get(dataFileStat.getDataFile_id());
						if (summary == null) {
							summary = new DataStatSummaryBean(dataFileStat.getDataFile_id());
							summariesByDataFileId.put(summary.getId(), summary);
						}
                        Long errorCount = this.getDataFileStatisticService().getErrorCountForStat(dataFileStat);

                        summary.addStat(dataFileStat, errorCount);
					}
					
					List<DataFileGroupBean> groups = new ArrayList<DataFileGroupBean>(); 
					Map<String, DataFileGroupBean> groupBySrcFile = new HashMap<String, DataFileGroupBean>(); 
					
					if (dataFiles != null) {
						for(DataFile dataFile : dataFiles) {
                            DataFileGroupBean topGroup = groupBySrcFile.get(dataFile.getSrcFile());
                            if (topGroup == null) {
                                topGroup = new DataFileGroupBean(dataFile.getId(), true);
                                topGroup.setSplitSrcFile(dataFile.getSrcFile());
                                groups.add(topGroup);
                                groupBySrcFile.put(topGroup.getSplitSrcFile(), topGroup);
                            }

                            DataFileGroupBean group = groupBySrcFile.get(dataFile.getSplitSrcFile());
							if (group == null) {
								group = new DataFileGroupBean(dataFile.getId(), false);
								group.setSplitSrcFile(dataFile.getSplitSrcFile());
								groupBySrcFile.put(group.getSplitSrcFile(), group);
                                topGroup.addChild(group);
							}
							
                            if (dataFile.getStatus() == FileStatus.TRANSFER_DONE) {
								DataStatSummaryBean summary = summariesByDataFileId.get(dataFile.getId());
								if (summary != null) {
									summary.setSplitSrcFile(dataFile.getFilePath());
									group.addChild(summary);
									groupBySrcFile.put(summary.getSplitSrcFile() + "_summary", summary);
									group = summary;
								}
							}
							
							DataFileBean bean = new DataFileBean(dataFile.getId());
							bean.setFilePath(dataFile.getFilePath());
							bean.setType(dataFile.getImportType());
							bean.setStatus(DataFileBean.Status.convert(dataFile.getStatus()));
							bean.setCreationDate(dataFile.getCreatedDate());
							bean.setStartDate(dataFile.getStartDate());
							bean.setEndDate(dataFile.getEndDate());
							bean.setStartTime(dataFile.getStartTime());
							bean.setEndTime(dataFile.getEndTime());
							bean.setRowInfo(dataFile.getCurrentRow(), dataFile.getNumErrorRows(), dataFile.getNumRows());
							bean.setModifiedDate(dataFile.getModifiedDate());
							bean.setHost(dataFile.getHost());
							
							group.addItem(bean);
						}

                        // Build flatten tree.
                        Set<String> uniqueIds = new HashSet<String>();
						Stack<DataFileGroupBean> groupStack = new Stack<DataFileGroupBean>();
						Stack<Integer> levelStack = new Stack<Integer>();
						List<DataFileGroupBean> processGroups = new ArrayList<DataFileGroupBean>(groups);
						Collections.reverse(processGroups);
						
						for(DataFileGroupBean group : processGroups) {
							groupStack.push(group);
							levelStack.push(0);
						}
						
						while(!groupStack.isEmpty()) {
							DataFileGroupBean node = groupStack.pop();
							int level = levelStack.pop();
							
							DataFileNodeBean nodeBean = new DataFileNodeBean(level, node);
							//System.out.printf("%d) %s\n", level, nodeBean.toString());
							if (uniqueIds.contains(nodeBean.getId())) {
								throw new RuntimeException("Found none unique identifier! " + nodeBean.getId());
							}
							uniqueIds.add(nodeBean.getId());
							nodes.add(nodeBean);
							
							nodeBean.setOpen(openNodeSet.contains(nodeBean.getId()));
							
							List<DataFileGroupBean> childProcessGroups = new ArrayList<DataFileGroupBean>(node.getChildren());
							Collections.reverse(childProcessGroups);
							
							for(DataFileGroupBean child : childProcessGroups) {
								groupStack.push(child);
								levelStack.push(level+1);
							}
							
							for(DataFileLeaf item : node.getItems()) {
								DataFileNodeBean itemBean = new DataFileNodeBean(level+1, item);
								
								if (uniqueIds.contains(itemBean.getId())) {
									throw new RuntimeException("Found none unique identifier! " + itemBean.getId());
								}
								
								uniqueIds.add(itemBean.getId());
								nodes.add(itemBean);
								itemBean.setOpen(openNodeSet.contains(itemBean.getId()));
							}
						}
					}
					
					model.addAttribute("retailerSiteFiles", nodes);
				} else {
					model.addAttribute("retailerSiteFiles", EMPTY_LIST);
				}
			} else {
				model.addAttribute("retailerSiteFiles", EMPTY_LIST);
			}
		} finally {
			
		}
	}
	
	private DataFilesDownloadManager getRetailerSiteDFDM(RetailerSite rs) {
		EnumDataImport edi = EnumDataImport.valueOf(rs.getProperties()
				.getIntegerProperty("retailerSite.dataImportType.id"));
		if (edi != null) {
			switch (edi) {
			case CJ:
				return this.cjDFDM;
			case FICSTAR:
				return this.ficstarDFDM;
			case GOOGLE:
				return this.googleDFDM;
			case MERCHANT:
				return this.merchantDFDM;
			default:
				return null;
			}
		} else {
			return null;
		}
	}

	protected DataFileStatisticService getDataFileStatisticService() {
		return this.dataFileStatistService;
	}

	@Autowired
	@Qualifier("dataFilesDownloadCJManagerImpl")
	private com.dell.acs.managers.DataFilesDownloadManager cjDFDM;

	@Autowired
	@Qualifier("dataFilesDownloadFicstarManagerImpl")
	private com.dell.acs.managers.DataFilesDownloadManager ficstarDFDM;

	@Autowired
	@Qualifier("dataFilesDownloadGoogleManagerImpl")
	private com.dell.acs.managers.DataFilesDownloadManager googleDFDM;

	@Autowired
	@Qualifier("dataFilesDownloadMerchantManagerImpl")
	private com.dell.acs.managers.DataFilesDownloadManager merchantDFDM;

	@Autowired
	private DataFileRepository dataFileRepository;

	@Autowired
	private RetailerSiteRepository retailerSiteRepository;

	@Autowired
	private UnvalidatedProductRepository unvalidatedProductRepository;

	@Autowired
	private DataFileStatisticService dataFileStatistService;

	public void setDataFileStatisticService(final DataFileStatisticService pDataFileStatistService) {
		this.dataFileStatistService = pDataFileStatistService;
	}
	
	@Autowired
	@Qualifier("hibernateSessionFactory")
	private SessionFactory sessionFactory;
}