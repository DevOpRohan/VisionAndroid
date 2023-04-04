package org.tensorflow.lite.examples.objectdetection;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010$\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010\'H\u0014J\b\u0010(\u001a\u00020%H\u0002J\b\u0010)\u001a\u00020%H\u0002J\b\u0010*\u001a\u00020%H\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u001c\u0010\b\u001a\u0004\u0018\u00010\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001c\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u001c\u0010\u001e\u001a\u0004\u0018\u00010\u001fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#\u00a8\u0006+"}, d2 = {"Lorg/tensorflow/lite/examples/objectdetection/cameraScene;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "cameraProviderFuture", "Lcom/google/common/util/concurrent/ListenableFuture;", "Landroidx/camera/lifecycle/ProcessCameraProvider;", "cameraSelector", "Landroidx/camera/core/CameraSelector;", "filePath", "Landroid/net/Uri;", "getFilePath", "()Landroid/net/Uri;", "setFilePath", "(Landroid/net/Uri;)V", "imageCapture", "Landroidx/camera/core/ImageCapture;", "imgCaptureExecutor", "Ljava/util/concurrent/ExecutorService;", "preview", "Landroidx/camera/view/PreviewView;", "getPreview", "()Landroidx/camera/view/PreviewView;", "setPreview", "(Landroidx/camera/view/PreviewView;)V", "storage", "Lcom/google/firebase/storage/FirebaseStorage;", "getStorage", "()Lcom/google/firebase/storage/FirebaseStorage;", "setStorage", "(Lcom/google/firebase/storage/FirebaseStorage;)V", "storageReference", "Lcom/google/firebase/storage/StorageReference;", "getStorageReference", "()Lcom/google/firebase/storage/StorageReference;", "setStorageReference", "(Lcom/google/firebase/storage/StorageReference;)V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "startCamera", "takePhoto", "uploadImage", "app_debug"})
public final class cameraScene extends androidx.appcompat.app.AppCompatActivity {
    private com.google.common.util.concurrent.ListenableFuture<androidx.camera.lifecycle.ProcessCameraProvider> cameraProviderFuture;
    private androidx.camera.core.CameraSelector cameraSelector;
    @org.jetbrains.annotations.Nullable()
    private androidx.camera.view.PreviewView preview;
    private java.util.concurrent.ExecutorService imgCaptureExecutor;
    private androidx.camera.core.ImageCapture imageCapture;
    @org.jetbrains.annotations.Nullable()
    private com.google.firebase.storage.FirebaseStorage storage;
    @org.jetbrains.annotations.Nullable()
    private com.google.firebase.storage.StorageReference storageReference;
    @org.jetbrains.annotations.Nullable()
    private android.net.Uri filePath;
    
    public cameraScene() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final androidx.camera.view.PreviewView getPreview() {
        return null;
    }
    
    public final void setPreview(@org.jetbrains.annotations.Nullable()
    androidx.camera.view.PreviewView p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.google.firebase.storage.FirebaseStorage getStorage() {
        return null;
    }
    
    public final void setStorage(@org.jetbrains.annotations.Nullable()
    com.google.firebase.storage.FirebaseStorage p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.google.firebase.storage.StorageReference getStorageReference() {
        return null;
    }
    
    public final void setStorageReference(@org.jetbrains.annotations.Nullable()
    com.google.firebase.storage.StorageReference p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void startCamera() {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.net.Uri getFilePath() {
        return null;
    }
    
    public final void setFilePath(@org.jetbrains.annotations.Nullable()
    android.net.Uri p0) {
    }
    
    private final void takePhoto() {
    }
    
    private final void uploadImage() {
    }
}