package com.example.pdedio.sendsnap.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.pdedio.sendsnap.BaseFragmentActivity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by pawel on 19.09.2016.
 */
@EBean
public class FragmentStackManager {


    @RootContext
    protected BaseFragmentActivity fragmentActivity;

    private FragmentManager fragmentManager;

    @AfterInject
    protected void afterInjectChangeFragmentManager() {
        this.fragmentManager = this.fragmentActivity.getSupportFragmentManager();
    }

    public void setInitialFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction transaction = this.fragmentManager.beginTransaction();
        if(this.getBackStackCount() > 0) {
            this.fragmentManager.popBackStack(this.fragmentManager.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        transaction.replace(containerViewId, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void replaceFragmentWithAddingToBackStack(int containerViewId, Fragment fragment, int enterAnimation, int exitAnimation, int popEnterAnimation, int popExitAnimation) {
        FragmentTransaction transaction = this.fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation);
        transaction.replace(containerViewId, fragment);

        if(transaction.isAddToBackStackAllowed()) {
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.commit();
    }

    public void replaceFragmentWithAddingToBackStack(int containerViewId, Fragment fragment) {

        FragmentTransaction transaction = this.fragmentManager.beginTransaction();
        transaction.replace(containerViewId, fragment);

        if(transaction.isAddToBackStackAllowed()) {
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.commit();
        this.fragmentManager.executePendingTransactions();
    }

    public void popBackStack() {
        this.fragmentManager.popBackStack();
    }

    public void popBackStackToRoot() {
        int backStackCount = this.getBackStackCount();

        if(backStackCount > 0) {
            FragmentManager.BackStackEntry bse =  this.fragmentManager.getBackStackEntryAt(0);
            String name = bse.getName();
            this.fragmentManager.popBackStackImmediate(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void replaceFragments(int containerViewId, Fragment oldFragment, Fragment newFragment) {
        FragmentTransaction transaction = this.fragmentManager.beginTransaction();
        transaction.remove(oldFragment);
        transaction.add(containerViewId, newFragment);

        if(transaction.isAddToBackStackAllowed()) {
            transaction.addToBackStack(newFragment.getClass().getName());
        }
        transaction.commit();
    }

    public int getBackStackCount() {
        return this.fragmentManager.getBackStackEntryCount();
    }

    public Fragment getActuallyDisplayedFragment(int containerViewId) {
        return this.fragmentManager.findFragmentById(containerViewId);
    }
}
