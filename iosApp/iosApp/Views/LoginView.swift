//
//  LoginView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 24/05/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import Foundation
import SwiftUI
import shared

class AuthStore: ObservableObject {
    @Published var hasFocus: Bool = true
    @Published var showLoading = false
    
    @Published var loginError: AppError?
}

struct LoginView: View {
    
    @EnvironmentObject var rootStore: RootStore
    @Environment(\.dismiss) var dismiss
    @StateObject private var store = AuthStore()
    
    @State private var signupPresented = false
    @State private var email = "anmol.verma4@gmail.com"
    @State private var password = "password"
    
    @FocusState private var focusedField: Bool
    
    private var loginError: Binding<Bool> {
        Binding {
            store.loginError != nil
        } set: { _ in
            store.loginError = nil
        }
    }
    
    var body: some View {
        VStack {
            credentialView
            footerView
        }
        .frame(width: UIScreen.main.bounds.width,
               height: UIScreen.main.bounds.height)
        .background(ColorAssets.colorBackground.color)
        .edgesIgnoringSafeArea(.all)
        .loadingIndicator(show: store.showLoading)
    }
    
    private var credentialView: some View {
        VStack {
            VStack {
                TextField("Work Email", text: $email)
                    .padding(.bottom)
                    .focused($focusedField)
                
                SecureField("Password", text: $password)
            }
            .padding(.horizontal)
            .textFieldStyle(RoundedBorderTextFieldStyle())
            
            signinButton
                .padding(.vertical)
        }
        .padding(.horizontal)
        .onChange(of: focusedField) { newValue in
            self.store.hasFocus = newValue
        }
        .onAppear {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                focusedField = self.store.hasFocus
            }
        }
        .alert(isPresented: loginError, error: store.loginError) {
            Text(store.loginError?.errorDescription ?? "")
        }
    }
    
    private var signinButton: some View {
        Button {
            performLogin()
        } label: {
            Text("Sign In")
                .harvestButton()
        }
    }
    
    var footerView: some View {
        VStack {
            HStack {
                Text("Don't have an account?")
                Button {
                    signupPresented = true
                } label: {
                    Text("Try Harvest Free")
                        .font(.headline)
                }
            }
            .padding(.bottom)
            
            Button {
                dismiss()
            } label: {
                Text("View Tour")
                    .font(.title3)
            }
        }
        .foregroundColor(ColorAssets.white.color)
        .sheet(isPresented: $signupPresented) {
            SignupView()
        }
    }
    
    private func performLogin() {
        LoginDataModel { state in
            if state is LoadingState {
                store.showLoading = true
                store.hasFocus = false
            } else {
                store.showLoading = false
                
                if let error = state as? ErrorState {
                    store.loginError = AppError(title: "Error",
                                                message: error.throwable.message ?? "Login failure")
                } else if let _ = state as? SuccessState<ApiResponse<HarvestOrganization>> {
                    rootStore.isAuthenticateUser = true
                }
            }
        }.login(email: email, password: password)
    }
}

#if DEBUG
struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView()
    }
}
#endif
