//
//  WorkspaceView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 07/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import shared
import SwiftUI

struct WorkspaceView_Previews: PreviewProvider {
    static var previews: some View {
        WorkspaceView(foundWorkspace: .constant(true))
    }
}

struct WorkspaceView: View {
    
    @Environment(\.colorScheme) private var colorScheme
    @Environment(\.dismiss) private var dismiss
    
    @Binding var foundWorkspace: Bool
    
    @State private var workspaceURLText = "MutualMobile"
    @State private var showLoader = false
    @State private var workspaceFindError: AppError?
    @State private var presentLogin = false
    
    private var workspaceError: Binding<Bool> {
        Binding {
            workspaceFindError != nil
        } set: { _ in
            workspaceFindError = nil
        }
    }
        
    var orgIdentifier: String {
        return workspaceURLText.lowercased()
    }
    
    var body: some View {
        VStack {
            VStack(alignment: .leading) {
                Text("Organisation Identifier: ")
                    .font(.footnote)
                
                HStack {
                    Text("https://")
                    TextField("your-workspace", text: $workspaceURLText)
                        .frame(width: 100)
                        .textCase(.lowercase)
                    Text(".harvestclone.com")
                }
                .font(.caption)
                
                Text("This is the org identifier you use to sign in to Harvest")
                    .font(.footnote)
            }
            .padding()
            
            Button {
                findWorkspace()
            } label: {
                Text("Find Workspace")
                    .harvestButton(color: ColorAssets.colorBackground.color)
            }
            .alert(isPresented: workspaceError, error: workspaceFindError) {
                Text(workspaceFindError?.errorDescription ?? "")
            }
        }
        .frame(width: UIScreen.main.bounds.width,
               height: UIScreen.main.bounds.height,
               alignment: .center)
        .loadingIndicator(show: showLoader)
    }
    
    private func findWorkspace() {
        if !workspaceURLText.isEmpty {
            let dataModel = FindOrgByIdentifierDataModel { state in
                print("state \(state)")
                if state is PraxisDataModel.LoadingState {
                    showLoader = true
                } else {
                    showLoader = false
                    if let error = state as? PraxisDataModel.ErrorState {
                        workspaceFindError = AppError(message: error.throwable.message ?? "Error while finding workspace")
                    } else if let networkResponse = state as? PraxisDataModelSuccessState<NetworkResponse<AnyObject>> {
                        print("networkResponse \(networkResponse)  \(type(of: networkResponse))")
                        dismiss()
                        foundWorkspace = true
                    }
                }
            }
            
            dataModel.findOrgByIdentifier(identifier: orgIdentifier)
        } else {
            workspaceFindError = AppError(message: "Please a enter your workspace")
        }
    }
}
