package com.bhsoft.ar3d.ui.base.viewmodel

import android.annotation.SuppressLint
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.bhsoft.ar3d.common.Constants
import com.bhsoft.ar3d.data.local.AppDatabase
import com.bhsoft.ar3d.data.model.api.IBaseResponse
import com.bhsoft.ar3d.data.remote.InteractCommon
import com.bhsoft.ar3d.ui.base.SingleLiveData
import com.bhsoft.ar3d.ui.base.callback.BaseCallBack
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference
import java.util.concurrent.Executor
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************

abstract class BaseViewModel<CallBack : BaseCallBack> : ViewModel {
    protected val appDatabase: AppDatabase
    protected val interactCommon: InteractCommon
    protected val schedulers: Executor
    var callBack: WeakReference<CallBack>? = null
    protected val mDiableAll: CompositeDisposable
    protected val mIsLoading = ObservableBoolean(false)
    private var mIsDestroy: Boolean
    val uiEventLiveData = SingleLiveData<Int>()

    constructor(appDatabase: AppDatabase,
                interactCommon: InteractCommon,
                schedulers: Executor){
        this.appDatabase = appDatabase
        this.schedulers = schedulers
        this.interactCommon = interactCommon
        this.mDiableAll = CompositeDisposable()
        mIsDestroy = false
    }
    override fun onCleared() {
        mDiableAll.dispose()
        super.onCleared()
    }

    fun isLoading() = mIsLoading
    fun setIsLoading(isLoading: Boolean) {
        mIsLoading.set(isLoading)
    }


    companion object {
        const val SHOW_TOAST = -2
        const val FINISH_ACTIVITY = -1
        const val BACK = 0
        const val SHOW_ERROR = 1

        fun checkExpire(data: IBaseResponse): Boolean {
            return data.getErrorCode() == Constants.CODE_TOKEN_EXPIRE || data.getStatus() == Constants.CODE_TOKEN_EXPIRE
        }

        fun checkExpire(data: Throwable): Boolean {
            if (data is retrofit2.HttpException) {
                if (data.code() == Constants.CODE_TOKEN_EXPIRE) {
                    return true
                }
            }
            return false
        }
    }


    protected fun <T : IBaseResponse> subscribeHasDispose(observable: Observable<T>?, onNext: (T) -> Unit, onError: (Throwable) -> Unit) {
        if (observable == null) {
            return
        }
        mDiableAll.add(observable.subscribe(
            {
                if (mIsDestroy) {
                    return@subscribe
                }

                onNext(it)
            },
            {
                run {
                    if (Constants.DEBUG) {
                        it.printStackTrace()

                    }
                    if (mIsDestroy) {
                        return@run
                    }
                }
                onError(it)
            }))

    }

    protected fun <T : MutableList<out IBaseResponse>> subscribeListHasDispose(observable: Observable<T>?, onNext: (T) -> Unit, onError: (Throwable) -> Unit) {
        if (observable == null) {
            return
        }
        mDiableAll.add(observable.subscribe(
            {
                if (mIsDestroy) {
                    return@subscribe
                }
                onNext(it)
            },
            {
                run {
                    if (Constants.DEBUG) {
                        it.printStackTrace()
                    }
                    if (mIsDestroy) {
                        return@run
                    }
                    onError(it)
                }
            }));

    }

    protected fun <T> subscribeListHasResultDispose(observable: Observable<T>?, onNext: (T) -> Unit, onError: (Throwable) -> Unit): Disposable? {
        if (observable == null) {
            return null
        }
        return observable.subscribe(
            {
                if (mIsDestroy) {
                    return@subscribe
                }

                onNext(it)
            },
            {
                run {
                    if (Constants.DEBUG) {
                        it.printStackTrace()
                    }
                    if (mIsDestroy) {
                        return@run;
                    }

                    onError(it)
                }
            })

    }


    @SuppressLint("CheckResult")
    protected fun <T> subscribeNotDispose(observable: Observable<T>?, onNext: (T) -> Unit, onError: (Throwable) -> Unit) {
        if (observable == null) {
            return
        }
        observable.subscribe(
            {
                if (mIsDestroy) {
                    return@subscribe
                }
                onNext(it)
            },
            {
                run {
                    if (Constants.DEBUG) {
                        it.printStackTrace();
                    }
                    if (mIsDestroy) {
                        return@run;
                    }
                    onError(it)
                }
            })
    }


    protected fun <T> subscribeHasResultDispose(observable: Observable<T>?, onNext: (T) -> Unit, onError: (Throwable) -> Unit): Disposable? {
        if (observable == null) {
            return null
        }
        return observable.subscribe(
            {
                if (mIsDestroy) {
                    return@subscribe
                }
                onNext(it)

            },
            {
                run {
                    if (Constants.DEBUG) {
                        it.printStackTrace()

                    }
                    if (mIsDestroy) {
                        return@run
                    }
                    onError(it)
                }
            })

    }

    protected fun makeFunOnOtherThread(method: () -> Unit) {
        Observable.create((ObservableOnSubscribe<Boolean> {
            method()
            it.onNext(true)
            it.onComplete()
        }))
            .subscribeOn(Schedulers.from(schedulers))
            .subscribe()
    }
}