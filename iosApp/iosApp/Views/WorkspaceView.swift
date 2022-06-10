//
//  WorkspaceView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 07/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import shared
import SwiftUI
import KMPNativeCoroutinesCombine
import Combine

struct WorkspaceView_Previews: PreviewProvider {
    static var previews: some View {
        WorkspaceView(foundWorkspace: .constant(true))
    }
}

class ViewModel : ObservableObject{
    let dataModel = FindOrgByIdentifierDataModel()
    @Published  var showLoader = false
    @Published  var workspaceFindError: AppError?
    var anyCancellable:AnyCancellable?
    
    func call(orgIdentifier : String,callback:@escaping  (Bool) -> (Void))  {
        anyCancellable?.cancel()
        anyCancellable = createPublisher(for: dataModel.dataFlowNative).receive(on: DispatchQueue.main).sink(receiveCompletion: { completion in
              debugPrint(completion)
          }, receiveValue: { [weak self] state in
              print("state \(state)")
              if state is PraxisDataModel.LoadingState {
                  
                  self?.showLoader = true
                                  } else {
                                      self?.showLoader = false
                                      if let error = state as? PraxisDataModel.ErrorState {
                                          self?.workspaceFindError = AppError(message: error.throwable.message ?? "Error while finding workspace")
                                      } else if let networkResponse = state as? PraxisDataModelSuccessState<NetworkResponse<AnyObject>> {
                                          print("networkResponse \(networkResponse)  \(type(of: networkResponse))")
                                          callback(true)
                                      }
                                  }
          })

          dataModel.findOrgByIdentifier(identifier: orgIdentifier)
    }

}

struct WorkspaceView: View {
    
    @Environment(\.colorScheme) private var colorScheme
    @Environment(\.dismiss) private var dismiss
    
    @Binding var foundWorkspace: Bool
 
    
    @ObservedObject var vm = ViewModel()

    @State private var workspaceURLText = "MutualMobile"
    @State private var presentLogin = false
    
    private var workspaceError: Binding<Bool> {
        Binding {
            vm.workspaceFindError != nil
        } set: { _ in
            vm.workspaceFindError = nil
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
            .alert(isPresented: workspaceError, error: vm.workspaceFindError) {
                Text(vm.workspaceFindError?.errorDescription ?? "")
            }
        }
        .frame(width: UIScreen.main.bounds.width,
               height: UIScreen.main.bounds.height,
               alignment: .center)
        .loadingIndicator(show: vm.showLoader)
    }
    
    func findWorkspace() {
        if !workspaceURLText.isEmpty {
            vm.call(orgIdentifier:orgIdentifier) { value in
                dismiss()
                foundWorkspace = value
            }
        } else {
            vm.workspaceFindError = AppError(message: "Please a enter your workspace")
        }
    }
}
