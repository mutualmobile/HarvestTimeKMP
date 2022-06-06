//
//  SignupView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 01/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI
import shared

class SignupStore: ObservableObject {
    @Published var hasFocus: Bool = true
    @Published var showLoading = false
    
    @Published var signupError: AppError?
}

struct SignupView: View {
    @ObservedObject private var store = SignupStore()

    @Environment(\.dismiss) var dismiss
    @Environment(\.colorScheme) private var colorScheme
    
    @State private var firstName = "tony"
    @State private var lastName = "stark"
    @State private var companyName = "Avenger"
    @State private var companyEmail = "tony.stark@gmail.com"
    @State private var password = "1234"
    
    private var signupError: Binding<Bool> {
        Binding {
            store.signupError != nil
        } set: { _ in
            store.signupError = nil
        }
    }
    init() {
        UINavigationBar.appearance().backgroundColor = UIColor(ColorAssets.colorBackground.color)
        UINavigationBar.appearance().tintColor = UIColor(ColorAssets.white.color)
    }
    
    var body: some View {
        NavigationView {
            VStack {
                googleSignInButton
                LabelledDivider(label: "or with you email below", color:
                                    colorScheme == . dark
                                ? ColorAssets.white.color
                                : .black)
                credentialView
                signunButton
                termsView
            }
            
            .navigationTitle("Sign Up")
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button {
                        dismiss()
                    } label: {
                        Text("Cancel")
                            .foregroundColor(ColorAssets.white.color)
                    }
                }
            }
        }
        .loadingIndicator(show: store.showLoading)
    }
    
    private var googleSignInButton: some View {
        Button {
            // TODO: (Nasir) Need to handle
        } label: {
            // TODO: (Nasir) Need to remove this entire HStack for Google Sign In, Must use button provided by Google pod
            HStack {
                Image("Google-Icon").padding(.trailing)
                Text("Sign In with Google").padding(.leading)
            }
            .harvestButton()
        }
        .padding(.bottom)
    }
    
    private var credentialView: some View {
        VStack {
            firstNameView
            lastNameView
            companyNameView
            companyEmailView
            passwordView
        }.padding(.horizontal)
    }
    
    private var firstNameView: some View {
        HStack {
            Text("First Name :")
            TextField("Tony", text: $firstName)
        }.padding(.vertical)
    }

    private var lastNameView: some View {
        HStack {
            Text("Last Name :")
            TextField("Stark", text: $lastName)
        }.padding(.bottom)
    }
    
    private var companyNameView: some View {
        HStack {
            Text("Company :")
            TextField("Stark Tech", text: $companyName)
        }.padding(.bottom)
    }
    
    private var companyEmailView: some View {
        HStack {
            Text("Email:")
            TextField("tony.stark@yyo.com", text: $companyEmail)
        }.padding(.bottom)
    }
    
    private var passwordView: some View {
        HStack {
            Text("Password: ")
            SecureField("tony.stark@yyo.com", text: $password)
        }.padding(.bottom)
    }
    
    private var signunButton: some View {
        Button {
            performSignup()
        } label: {
            Text("SIGN UP")
                .harvestButton(color: ColorAssets.colorBackground.color)
        }
        .alert(isPresented: signupError, error: store.signupError) {
            Text(store.signupError?.errorDescription ?? "")
        }
    }
    
    private var termsView: some View {
        VStack {
            Text("By signing up you agree to the")
            HStack {
                Button {
                    // TODO: Handle Terms of service action later
                } label: {
                    Text("Terms of Service")
                }
                Button {
                    // TODO: Handle privacy action later
                } label: {
                    Text("Privacy policy")
                }
            }.foregroundColor(.orange)
        }.padding()
    }
    
    private func performSignup() {
        SignUpDataModel { state in
            
            print("state \(state)")
            
            if state is LoadingState {
                store.showLoading = true
                store.hasFocus = false
            } else {
                store.showLoading = false
                if let error = state as? ErrorState {
                    store.signupError = AppError(title: "Error",
                                                 message: error.throwable.message ?? "Signup failure")
                } else if state is SuccessState<AnyObject> {
                    
                }
            }
        }.signUp(firstName: firstName,
                 lastName: lastName,
                 company: companyName,
                 email: companyEmail,
                 password: password)
    }
}

struct SignupView_Previews: PreviewProvider {
    static var previews: some View {
        SignupView()
    }
}
