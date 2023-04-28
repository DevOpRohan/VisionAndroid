package org.tensorflow.lite.examples.objectdetection;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0012\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0014J\u0010\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u0013H\u0002J\u0010\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u001b\u001a\u00020\u0013H\u0002J\b\u0010\u001d\u001a\u00020\u0017H\u0002J\b\u0010\u001e\u001a\u00020\u0017H\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u001c\u0010\f\u001a\u0004\u0018\u00010\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011\u00a8\u0006\u001f"}, d2 = {"Lorg/tensorflow/lite/examples/objectdetection/cameraScene;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "cameraProviderFuture", "Lcom/google/common/util/concurrent/ListenableFuture;", "Landroidx/camera/lifecycle/ProcessCameraProvider;", "cameraSelector", "Landroidx/camera/core/CameraSelector;", "imageCapture", "Landroidx/camera/core/ImageCapture;", "imgCaptureExecutor", "Ljava/util/concurrent/ExecutorService;", "preview", "Landroidx/camera/view/PreviewView;", "getPreview", "()Landroidx/camera/view/PreviewView;", "setPreview", "(Landroidx/camera/view/PreviewView;)V", "imageProxyToBitmap", "Landroid/graphics/Bitmap;", "image", "Landroidx/camera/core/ImageProxy;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "processImage", "bitmap", "sendImage", "startCamera", "takePhoto", "app_debug"})
public final class cameraScene extends androidx.appcompat.app.AppCompatActivity {
    private com.google.common.util.concurrent.ListenableFuture<androidx.camera.lifecycle.ProcessCameraProvider> cameraProviderFuture;
    private androidx.camera.core.CameraSelector cameraSelector;
    @org.jetbrains.annotations.Nullable
    private androidx.camera.view.PreviewView preview;
    private java.util.concurrent.ExecutorService imgCaptureExecutor;
    private androidx.camera.core.ImageCapture imageCapture;
    
    public cameraScene() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final androidx.camera.view.PreviewView getPreview() {
        return null;
    }
    
    public final void setPreview(@org.jetbrains.annotations.Nullable
    androidx.camera.view.PreviewView p0) {
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void startCamera() {
    }
    
    private final void takePhoto() {
    }
    
    private final android.graphics.Bitmap imageProxyToBitmap(androidx.camera.core.ImageProxy image) {
        return null;
    }
    
    private final android.graphics.Bitmap processImage(android.graphics.Bitmap bitmap) {
        return null;
    }
    
    private final void sendImage(android.graphics.Bitmap bitmap) {
    }
}