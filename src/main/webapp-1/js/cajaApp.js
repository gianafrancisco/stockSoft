function cajaController($scope,$http,$window,$location,$rootScope) {

    $scope.caja = {};

     $scope.abrir = function(){

         var request = {
            username: $scope.vendedor.username
         };

         $http.put("/caja/abrir",request)
         .success(function(data, status, headers, config) {
             $scope.caja=data;
             $scope.isOpen();
             $scope.resumen();
         });
     };

     $scope.cerrar = function(){

         var request = {
            username: $scope.vendedor.username
         };

         $http.put("/caja/cerrar",request)
         .success(function(data, status, headers, config) {
             $scope.caja=data;
             $scope.isOpen();
             if($scope.caja.cajaId != undefined){
                $scope.imprimir($scope.caja.cajaId);
             }
         });
     };

      $scope.isOpen = function(){

          $http.put("/caja/abierta")
          .success(function(data, status, headers, config) {
              $rootScope.cajaAbierta=data;
          });
      };

      $scope.resumen = function(){

           if($scope.vendedor.username != undefined){
               var request = {
                  username: $scope.vendedor.username
               };

              $http.put("/caja/resumen",request)
              .success(function(data, status, headers, config) {
                  $scope.caja=data;
              });
           }
      };


       $scope.obtenerListaVendedores = function(callback){
           var url = "/vendedores?page=0&size=1000";
           $http.get(url)
           .success(function(data, status, headers, config) {
               $scope.vendedores=data;
               /*
               if($scope.vendedores.content.length > 0){
                   $scope.vendedor = $scope.vendedores.content[0];
                   callback();
               }*/
           });
       };

      $scope.imprimir = function(cajaId){
          $http.put("/caja/imprimir/"+cajaId)
          .success(function(data, status, headers, config) {
              $scope.imprimir=data;
          });
      };

    $scope.print = function(){
        window.print();
    };

     $scope.init = function(){
        $scope.isOpen();
        $scope.obtenerListaVendedores(function(){
            //$scope.resumen();
        });

     };
     $scope.init();

 };