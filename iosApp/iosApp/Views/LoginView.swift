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
import KMPNativeCoroutinesCombine
import Combine

class AuthStore: ObservableObject {
    @Published var hasFocus: Bool = true
    @Published var showLoading = false
    @Published var loginError: AppError?
    
    @Published var email = "anmol.verma4@gmail.com"
    @Published var password = "password"
    
    var anyCancellable: AnyCancellable? = nil

    let loginDataModel = LoginDataModel()
    
    init() {
        loginDataModel.activate()
    }
    
    func login(callback:@escaping  () -> (Void))  {
        anyCancellable =  createPublisher(for: loginDataModel.dataFlowNative).sink { completion in
            debugPrint(completion)
        } receiveValue: { [self] state in
               if state is PraxisDataModel.LoadingState {
                   showLoading = true
                   hasFocus = false
               } else {
                   showLoading = false
                   
                   if let error = state as? PraxisDataModel.ErrorState {
                       loginError = AppError(title: "Error",
                                                   message: error.throwable.message ?? "Login failure")
                   } else if let responseState = state as? PraxisDataModelSuccessState<ApiResponse<HarvestOrganization>> {
                      callback()
                   }
               }
        }
        loginDataModel.login(email: email, password: password)
    }

}

struct LoginView: View {

    @EnvironmentObject var rootStore: RootStore
    @Environment(\.dismiss) var dismiss
    @StateObject private var store = AuthStore()
    
    @State private var signupPresented = false
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
        .loadingIndicator(show: store.showLoading).onDisappear {
            self.store.anyCancellable?.cancel()
        }
    }
    
    private var credentialView: some View {
        VStack {
            VStack {
                TextField("Work Email", text: $store.email)
                    .padding(.bottom)
                    .focused($focusedField)
                
                SecureField("Password", text: $store.password)
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
        store.login() {
            rootStore.isAuthenticateUser = true
        }
    }
}

#if DEBUG
struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView()
    }
}
#endif
