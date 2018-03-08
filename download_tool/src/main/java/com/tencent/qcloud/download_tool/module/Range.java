package com.tencent.qcloud.download_tool.module;

import java.util.Locale;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public class Range {

    private long start;
    private long end;
    public Range(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public Range(long start) {
        this.start = start;
        this.end = -1L;
    }

    public long getStart() {
        return this.start;
    }

    public long getEnd() {
        return this.end;
    }

    public String toString() {
        String endString = this.end == -1L?"":String.valueOf(this.end);
        return String.format(Locale.ENGLISH, "bytes=%d-%s", new Object[]{Long.valueOf(this.start), endString});
    }
}
