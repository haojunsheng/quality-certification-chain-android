package com.waibao.qualityCertification.view;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

public class RectangleCameraResultCallback implements ResultPointCallback {
    private final RectangleCameraView rectangleCameraView;

    public RectangleCameraResultCallback(RectangleCameraView rectangleCameraView) {
        this.rectangleCameraView = rectangleCameraView;
    }

    public void foundPossibleResultPoint(ResultPoint point) {
        rectangleCameraView.addPossibleResultPoint(point);
    }

}
