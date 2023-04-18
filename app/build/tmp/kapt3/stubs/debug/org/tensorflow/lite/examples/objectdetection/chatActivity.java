package org.tensorflow.lite.examples.objectdetection;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u000e\u0010/\u001a\u00020*2\u0006\u00100\u001a\u00020*J\u0006\u00101\u001a\u000202J\"\u00103\u001a\u0002022\u0006\u00104\u001a\u0002052\u0006\u00106\u001a\u0002052\b\u00107\u001a\u0004\u0018\u00010\u000fH\u0014J\u0012\u00108\u001a\u0002022\b\u00109\u001a\u0004\u0018\u00010:H\u0014J\b\u0010;\u001a\u000202H\u0016J\u0010\u0010<\u001a\u0002022\u0006\u0010=\u001a\u000205H\u0016J\b\u0010>\u001a\u000202H\u0014J\u000e\u0010?\u001a\u0002022\u0006\u0010@\u001a\u00020\bJ\u000e\u0010A\u001a\u0002022\u0006\u0010B\u001a\u000205J\u0010\u0010C\u001a\u0002022\u0006\u00100\u001a\u00020*H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R \u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR \u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\u00020\u0015X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001e\u001a\u00020\u000fX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u000e\u0010#\u001a\u00020$X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\'\u001a\u0004\u0018\u00010(X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010)\u001a\u0004\u0018\u00010*X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.\u00a8\u0006D"}, d2 = {"Lorg/tensorflow/lite/examples/objectdetection/chatActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Landroid/speech/tts/TextToSpeech$OnInitListener;", "()V", "Editor", "Landroid/content/SharedPreferences$Editor;", "MessageArray", "Ljava/util/ArrayList;", "Lcom/prianshuprasad/assistant/messageData;", "getMessageArray", "()Ljava/util/ArrayList;", "setMessageArray", "(Ljava/util/ArrayList;)V", "activityResultLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "Landroid/content/Intent;", "getActivityResultLauncher", "()Landroidx/activity/result/ActivityResultLauncher;", "setActivityResultLauncher", "(Landroidx/activity/result/ActivityResultLauncher;)V", "ll", "Landroidx/recyclerview/widget/LinearLayoutManager;", "getLl", "()Landroidx/recyclerview/widget/LinearLayoutManager;", "setLl", "(Landroidx/recyclerview/widget/LinearLayoutManager;)V", "mAdapter", "Lorg/tensorflow/lite/examples/objectdetection/adapter;", "rcview", "Landroidx/recyclerview/widget/RecyclerView;", "speechIntent", "getSpeechIntent", "()Landroid/content/Intent;", "setSpeechIntent", "(Landroid/content/Intent;)V", "speechRecognizer", "Landroid/speech/SpeechRecognizer;", "storage", "Landroid/content/SharedPreferences;", "tts", "Landroid/speech/tts/TextToSpeech;", "usernamei", "", "getUsernamei", "()Ljava/lang/String;", "setUsernamei", "(Ljava/lang/String;)V", "API", "str", "Talk", "", "onActivityResult", "requestCode", "", "resultCode", "data", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onInit", "status", "onResume", "onitemclicked", "messageData", "scrolltoPos", "x", "speakOut", "app_debug"})
public final class chatActivity extends androidx.appcompat.app.AppCompatActivity implements android.speech.tts.TextToSpeech.OnInitListener {
    public android.content.Intent speechIntent;
    private android.speech.SpeechRecognizer speechRecognizer;
    private android.speech.tts.TextToSpeech tts;
    public androidx.activity.result.ActivityResultLauncher<android.content.Intent> activityResultLauncher;
    private org.tensorflow.lite.examples.objectdetection.adapter mAdapter;
    public androidx.recyclerview.widget.LinearLayoutManager ll;
    @org.jetbrains.annotations.Nullable
    private java.lang.String usernamei = "";
    private androidx.recyclerview.widget.RecyclerView rcview;
    private android.content.SharedPreferences storage;
    private android.content.SharedPreferences.Editor Editor;
    @org.jetbrains.annotations.NotNull
    private java.util.ArrayList<com.prianshuprasad.assistant.messageData> MessageArray;
    
    public chatActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final android.content.Intent getSpeechIntent() {
        return null;
    }
    
    public final void setSpeechIntent(@org.jetbrains.annotations.NotNull
    android.content.Intent p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.activity.result.ActivityResultLauncher<android.content.Intent> getActivityResultLauncher() {
        return null;
    }
    
    public final void setActivityResultLauncher(@org.jetbrains.annotations.NotNull
    androidx.activity.result.ActivityResultLauncher<android.content.Intent> p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.recyclerview.widget.LinearLayoutManager getLl() {
        return null;
    }
    
    public final void setLl(@org.jetbrains.annotations.NotNull
    androidx.recyclerview.widget.LinearLayoutManager p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getUsernamei() {
        return null;
    }
    
    public final void setUsernamei(@org.jetbrains.annotations.Nullable
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.ArrayList<com.prianshuprasad.assistant.messageData> getMessageArray() {
        return null;
    }
    
    public final void setMessageArray(@org.jetbrains.annotations.NotNull
    java.util.ArrayList<com.prianshuprasad.assistant.messageData> p0) {
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String API(@org.jetbrains.annotations.NotNull
    java.lang.String str) {
        return null;
    }
    
    public final void Talk() {
    }
    
    @java.lang.Override
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable
    android.content.Intent data) {
    }
    
    @java.lang.Override
    public void onInit(int status) {
    }
    
    private final void speakOut(java.lang.String str) {
    }
    
    @java.lang.Override
    public void onDestroy() {
    }
    
    public final void onitemclicked(@org.jetbrains.annotations.NotNull
    com.prianshuprasad.assistant.messageData messageData) {
    }
    
    @java.lang.Override
    protected void onResume() {
    }
    
    public final void scrolltoPos(int x) {
    }
}