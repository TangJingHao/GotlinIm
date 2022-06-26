package com.ByteDance.Gotlin.im.viewmodel;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ByteDance.Gotlin.im.view.activity.ChatActivity;

/**
 * @author: Hx
 * @date: 2022年06月17日 13:53
 */
public class ChatViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChatViewModel.class)) {
            return (T) new ChatViewModel(ChatActivity.getSession());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
