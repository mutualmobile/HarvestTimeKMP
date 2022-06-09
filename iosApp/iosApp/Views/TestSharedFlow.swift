//
//  TestSharedFlow.swift
//  iosApp
//
//  Created by Anmol Verma on 09/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import Foundation
import shared
import Combine
import KMPNativeCoroutinesCombine

class Test{
    var anyCancellable: AnyCancellable? = nil

    let dataModel = AssignProjectsToUsersDataModel { DataState in
        
    }
    
    func assign()  {
        dataModel.activate()
        
        anyCancellable = createPublisher(for: dataModel.assignProjectsNative).receive(on: DispatchQueue.main).sink(receiveCompletion: { completion in
                   debugPrint(completion)
               }, receiveValue: { response in
                   if response is LoadingState {
                       print("loading....")
                   }
                  
               })
    }
    
}
