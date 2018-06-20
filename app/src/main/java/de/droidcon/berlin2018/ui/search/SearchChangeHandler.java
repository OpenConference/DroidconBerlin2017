package de.droidcon.berlin2018.ui.search;

import android.annotation.TargetApi;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import de.droidcon.berlin2018.R;
import de.droidcon.berlin2018.ui.changehandler.SharedElementTransitionChangeHandler;
import de.droidcon.berlin2018.ui.searchbox.SearchBox;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

@TargetApi(Build.VERSION_CODES.LOLLIPOP) public class SearchChangeHandler
    extends SharedElementTransitionChangeHandler {

  private static final String KEY_WAIT_FOR_TRANSITION_NAMES = "SearchChangeHandler.names";

  private final ArrayList<String> names;

  public SearchChangeHandler() {
    names = new ArrayList<>();
  }

  public SearchChangeHandler(@NonNull List<String> waitForTransitionNames) {
    names = new ArrayList<>(waitForTransitionNames);
  }

  @Override public void saveToBundle(@NonNull Bundle bundle) {
    bundle.putStringArrayList(KEY_WAIT_FOR_TRANSITION_NAMES, names);
  }

  @Override public void restoreFromBundle(@NonNull Bundle bundle) {
    List<String> savedNames = bundle.getStringArrayList(KEY_WAIT_FOR_TRANSITION_NAMES);
    if (savedNames != null) {
      names.addAll(savedNames);
    }
  }

  @Nullable public Transition getExitTransition(@NonNull ViewGroup container, @Nullable View from,
      @Nullable View to, boolean isPush) {
    if (isPush) return null;

    return new Fade().excludeTarget(R.id.searchBox, true);
  }

  @Override @Nullable
  public Transition getSharedElementTransition(@NonNull ViewGroup container, @Nullable View from,
      @Nullable final View to, boolean isPush) {
    TransitionSet transitionSet = new TransitionSet().addTransition(new ChangeBounds())
        .addTransition(new ChangeClipBounds())
        .addTransition(new ChangeTransform())
        .addTarget(container.getResources().getString(R.string.transition_searchbox));

    if (isPush) {


      // Set the transition drawable for animation
      to.setBackground(to.getResources().getDrawable(R.drawable.search_background));

      transitionSet.addListener(new Transition.TransitionListener() {

        @Override public void onTransitionStart(Transition transition) {
          try {
            final SearchBox searchBox = (SearchBox) to.findViewById(R.id.searchBox);
            searchBox.animateSearchIconToCloseIcon();
            TransitionDrawable background = (TransitionDrawable) to.getBackground();
            background.startTransition(200);
          } catch (Throwable t){
            Timber.e(t);
          }
        }

        @Override public void onTransitionEnd(Transition transition) {

        }

        @Override public void onTransitionCancel(Transition transition) {

        }

        @Override public void onTransitionPause(Transition transition) {

        }

        @Override public void onTransitionResume(Transition transition) {

        }
      });
    }

    return transitionSet;
  }

  @Override @Nullable
  public Transition getEnterTransition(@NonNull ViewGroup container, @Nullable View from,
      @Nullable View to, boolean isPush) {
    return null;
  }

  @Override public void configureSharedElements(@NonNull ViewGroup container, @Nullable View from,
      @Nullable View to, boolean isPush) {
    for (String name : names) {
      addSharedElement(name);
      waitOnSharedElementNamed(name);
    }
  }

  @Override public boolean removesFromViewOnPush() {
    return false;
  }
}
