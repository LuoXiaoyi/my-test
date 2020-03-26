package com.perfma.license;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class License{
    private long startTime;
    private long endTime;
    private long currentTime;
    private long remainTime;

    /**
     * @param timeInfo 从底层获取的 License 信息
     * 每8个 byte 表示一个时间，顺序依次为
     * 开始时间，当前时间，结束时间
     */
    public License(byte[] timeInfo){
        if(timeInfo == null || timeInfo.length != 24){
            throw new IllegalArgumentException("time info format error.");
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(timeInfo);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        startTime = byteBuffer.getLong();
        currentTime = byteBuffer.getLong();
        endTime = byteBuffer.getLong();
        remainTime = endTime - currentTime;
    }

    public long getStartTime(){
        return this.startTime;
    }

    public long getEndTime(){
        return this.endTime;
    }

    public long getCurrentTime(){
        return this.currentTime;
    }

    public long getRemainTime(){
        return this.remainTime;
    }

    public String toString(){
        int _DAY_ = 86400;
        StringBuffer ret = new StringBuffer();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date s = new Date(this.getStartTime());
        ret.append("start   time: " + df.format(s) + "\n");
        s = new Date(this.getCurrentTime());
        ret.append("current time: " + df.format(s) + "\n");
        s = new Date(this.getEndTime());
        ret.append("end     time: " + df.format(s) + "\n");
        long r = this.getRemainTime() / 1000;
        ret.append("remain  time: " + r / _DAY_ + " days," + (r % _DAY_) / 3600 + " hours," +
                ((r % _DAY_) % 3600) / 60 + " mins, " + ((r % _DAY_) % 3600) % 60 + " secs \n");

        return ret.toString();
    }
}