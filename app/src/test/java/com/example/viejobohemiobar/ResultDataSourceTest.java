package com.example.viejobohemiobar;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;


import com.example.viejobohemiobar.dataSource.ResultDataSource;
import com.example.viejobohemiobar.model.pojo.Result;
import com.example.viejobohemiobar.service.RetrofitInstance;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

public class ResultDataSourceTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    public RetrofitInstance retrofitInstance;

    @InjectMocks
    public ResultDataSource resultDataSource = new ResultDataSource();
    private Single<Result> resultSingleTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupRxSchedulers();
    }

    @Test
    public void testSuccess() {
        Result result = new Result();
        resultSingleTest = Single.just(result);
        Mockito.when(retrofitInstance.getApiService(ResultDataSource.PATH,0)).thenReturn(resultSingleTest);
        resultDataSource.refreshGetProducts();
        Assert.assertEquals(result, resultDataSource.liveResult.getValue());
        Assert.assertEquals(false, resultDataSource.error.getValue());
    }

    @Test
    public void testError() {
        resultSingleTest = Single.error(Throwable::new);
        Mockito.when(retrofitInstance.getApiService(ResultDataSource.PATH,0)).thenReturn(resultSingleTest);
        resultDataSource.refreshGetProducts();
        Assert.assertEquals(true, resultDataSource.error.getValue());
    }

    private void setupRxSchedulers() {
        Scheduler immediate = new Scheduler() {
            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(schedulerCallable -> immediate);
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);
    }


}
