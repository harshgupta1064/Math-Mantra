package com.zendalona.mathmantra.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zendalona.mathmantra.MainActivity;
import com.zendalona.mathmantra.databinding.FragmentNarratorBinding;
import com.zendalona.mathmantra.utils.TTSUtility;

import java.util.ArrayList;

public class NarratorFragment extends Fragment {

    private FragmentNarratorBinding binding;
    private TTSUtility tts;

    private ArrayList<String> theoryContents;

    private int currentIndex = 0;

    public NarratorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TTSUtility(requireContext());
        tts.setSpeechRate(0.9f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNarratorBinding.inflate(inflater, container, false);
        Bundle args = getArguments();
        assert args != null;
        if(!args.isEmpty()){
            theoryContents = args.getStringArrayList("contents");
        }

        updateTheoryContent();

        binding.repeatButton.setOnClickListener(v -> tts.speak(binding.theoryText.getText().toString()));

        binding.previousButton.setOnClickListener(v -> {
            if(currentIndex == 0) currentIndex++;
            currentIndex = (currentIndex - 1) % theoryContents.size();
            updateTheoryContent();
        });

        binding.nextButton.setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % theoryContents.size();
            updateTheoryContent();
        });
        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Navigate to MainActivity
                        Intent intent = new Intent(requireActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                });
    }

    private void updateTheoryContent() {
        String content = theoryContents.get(currentIndex);
        binding.theoryText.setText(content);
        tts.speak(content);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        tts.shutdown();
    }
}