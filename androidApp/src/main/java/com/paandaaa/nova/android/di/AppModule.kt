package com.paandaaa.nova.android.di

import android.content.Context
import androidx.room.Room
import com.paandaaa.nova.android.data.repository.OnboardingRepositoryImpl
import com.paandaaa.nova.android.data.repository.AuthRepositoryImpl
import com.paandaaa.nova.android.domain.repository.AuthRepository
import com.paandaaa.nova.android.domain.repository.OnboardingRepository
import com.paandaaa.nova.android.domain.usecase.auth.AuthUseCases
import com.paandaaa.nova.android.domain.usecase.auth.GetCurrentUserUseCase
import com.paandaaa.nova.android.domain.usecase.auth.GetIdTokenUseCase
import com.paandaaa.nova.android.domain.usecase.auth.IsAuthenticatedUseCase
import com.paandaaa.nova.android.domain.usecase.auth.SignInWithGoogleUseCase
import com.paandaaa.nova.android.domain.usecase.auth.SignOutUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import java.nio.channels.FileChannel
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        client: SupabaseClient // Hilt will provide this
    ): AuthRepository = AuthRepositoryImpl(client)

    @Provides
    @Singleton
    fun provideAuthUseCases(
        signIn: SignInWithGoogleUseCase,
        isAuthenticated: IsAuthenticatedUseCase,
        getUser: GetCurrentUserUseCase,
        signOut: SignOutUseCase,
        getIdToken: GetIdTokenUseCase
    ): AuthUseCases = AuthUseCases(
        signInWithGoogle = signIn,
        isAuthenticated = isAuthenticated,
        getCurrentUser = getUser,
        signOut = signOut,
        getIdToken = getIdToken
    )

    @Provides
    @Singleton
    fun provideSignInWithGoogleUseCase(
        authRepository: AuthRepository
    ): SignInWithGoogleUseCase {
        return SignInWithGoogleUseCase(authRepository = authRepository)
    }

    @Provides
    @Singleton
    fun provideIsAuthenticatedUseCase(authRepository: AuthRepository): IsAuthenticatedUseCase {
        return IsAuthenticatedUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(authRepository: AuthRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideSignOutUseCase(authRepository: AuthRepository): SignOutUseCase {
        return SignOutUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideGetIdTokenUseCase(authRepository: AuthRepository): GetIdTokenUseCase {
        return GetIdTokenUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideOnboardingRepository(): OnboardingRepository = OnboardingRepositoryImpl()





    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

}