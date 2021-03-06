package com.epam.lab.spider.job.limit;

/**
 * @author Yura Kovalik
 */
public interface UserLimitProcessor {
    void markTaskExecute(Integer userId);
    void markPostExecute(Integer userId);
    void markAttachmentExecute(Integer userId);

    boolean checkTaskExecute(Integer userId);
    boolean checkPostExecute(Integer userId);
    boolean checkAttachmentExecute(Integer userId);


    int getRemainderTaskExecute(Integer userId);
    int getRemainderPostExecute(Integer userId);
    int getRemainderAttachmentExecute(Integer userId);

    int getRemainderTaskExecuteInPercent(Integer userId);
    int getRemainderPostExecuteInPercent(Integer userId);
    int getRemainderAttachmentExecuteInPercent(Integer userId);

    void markAttachmentTraffic(Integer userId, int traffic);
    boolean checkAttachmentTraffic(Integer userId);
    boolean checkAttachmentTraffic(Integer userId, int withNextTraffic);
    int getRemainderAttachmentTraffic(Integer userId);
    int getRemainderAttachmentTraffic(Integer userId, int withNextTraffic);
    int getRemainderAttachmentTrafficInPercent(Integer userId);
    int getRemainderAttachmentTrafficInPercent(Integer userId, int withNextTraffic);

}
