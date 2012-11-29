package com.dell.acs.jobs;

import com.dell.acs.managers.ImageDownloadManager;
import com.dell.acs.persistence.domain.ProductImage;
import com.dell.acs.stats.*;
import com.sourcen.core.jobs.AbstractJob;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.UrlUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Adarsh
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1595 $, $Date:: 5/15/12 12:05 PM#$
 */
public class SrcImageDownloadJob extends AbstractJob {

    private static final Logger logger = Logger.getLogger(SrcImageDownloadJob.class);
    private static AtomicBoolean jobRunningStatus = new AtomicBoolean(false);
    private static final String successfulMsg = " : Downloaded successful and updated for Product Image  :- ";
    private static final String failureMsg = " : Downloaded fails and updated for Error status for Product Image :- ";

    @Override
    protected void executeJob(JobExecutionContext context) {
        CountStatMutator count = null;
        CountStatMutator imageCount = null;
        TimerStatMutator imageTimer = null;
        try {
            count = (CountStatMutator) StatUtil.getInstance().getStat(CountStat.class, "SrcImageDownloadJob.Count");
            count.inc();
            count.apply();
            if (!jobRunningStatus.get()) {
                synchronized (jobRunningStatus) {
                    jobRunningStatus.set(true);
                    final Collection<ProductImage> productImagesList = imageDownloadManager.getProductImagesByMatch("cached", 0, 100);
                    if (productImagesList != null) {
                        imageCount = (CountStatMutator) StatUtil.getInstance().getStat(CountMinMaxStat.class, "SrcImageDownloadJob.ImageCount");
                        imageCount.incBy(productImagesList.size());
                        imageTimer = (TimerStatMutator) StatUtil.getInstance().getStat(TimerStat.class, "SrcImageDownloadJob.ImageTimer");
                        imageTimer.start();

                        for (ProductImage productImage : productImagesList) {
                            Assert.notNull(productImage.getProduct(),"Product is Null for Product Image Updateing for invalid Row "+productImage.getId());
                            logger.info("Image Download Started for :- "+ productImage.getId());
                            String srcUrl = productImage.getSrcImageURL();
                            String destinationUrl = productImage.getImageURL();
                            Assert.notNull(srcUrl.trim(),"Source Image Url is Null "+productImage.getId());
                            if (destinationUrl.contains("?")) {
                                destinationUrl = destinationUrl.substring(0, destinationUrl.indexOf("?"));
                            }
                            File destinationFile = configurationService.getFileSystem().getFile(destinationUrl, false, false);
                            try {
                                /* download the images and cache */
                                if (!destinationFile.exists() || destinationFile.length() == 0) {
                                  /*  downloadImage(srcUrl, destinationFile);*/
                                    URL srcUrl1 = UrlUtils.buildUri(srcUrl).toURL();
                                    FileUtils.copyURLToFile(srcUrl1, destinationFile);
                                    imageDownloadManager.updateDownloadedImage(productImage, 1);
                                    logger.info(successfulMsg + productImage.getId());
                                }else{
                                    logger.info("Destination File exists, Updating Image cache :- " + productImage.getId());
									imageDownloadManager.updateDownloadedImage(productImage, 1);
                                }
                            } catch (MalformedURLException e) {
                                logger.error(failureMsg+productImage.getId()+" " + srcUrl);
                                //update  it to status error
                                imageDownloadManager.updateDownloadedImage(productImage, 2);
                            } catch (UnsupportedEncodingException unSupEx) {
                                logger.error("Unsupported encoding. for Product image id :- "+ productImage.getId(), unSupEx);
                                logger.error(failureMsg+productImage.getId()+" " + srcUrl);
                                //update  it to status error
                                imageDownloadManager.updateDownloadedImage(productImage, 2);
                            } catch (IOException ioEx) {
                                logger.error(failureMsg+productImage.getId()+" " + srcUrl);
                                //update  it to status error
                                logger.error("Unable to read/write/access the image for Product image id :- "+ productImage.getId(), ioEx);
                                imageDownloadManager.updateDownloadedImage(productImage, 2);
                            } catch (Exception e) {
                                logger.error(failureMsg+productImage.getId()+" " + srcUrl);
                                logger.error("Unable to Download for Product image id :- "+ productImage.getId() + e.getMessage(), e);
                                //update  it to status error
                                imageDownloadManager.updateDownloadedImage(productImage, 2);
                            }

                        }
                    }
                }
            }
        }catch (IllegalArgumentException illArgsExcp){
            logger.error(illArgsExcp.getMessage());
        } catch (Exception e) {
            logger.error("Exception :- " + e.getMessage() + e);
        }

        finally {
            // Reset the jobRunningStatus flag once done with processing all broken images
            jobRunningStatus.set(false);
            if (imageTimer != null) {
                imageTimer.stop();
                imageTimer.apply();
            }
            if (imageCount != null) {
                imageCount.apply();
            }
            if (count != null) {
                count.clear();
                count.dec();
                count.apply();
            }
        }
    }

    private void downloadImage(String downloadImageUrl, File destinationFile) throws Exception {
        URL srcUrl = UrlUtils.buildUri(downloadImageUrl).toURL();
        FileUtils.copyURLToFile(srcUrl, destinationFile);
    }


    @Autowired
    private ImageDownloadManager imageDownloadManager;

    public void setImageDownloadManager(ImageDownloadManager imageDownloadManager) {
        this.imageDownloadManager = imageDownloadManager;
    }

}
