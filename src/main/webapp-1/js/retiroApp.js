function retiroController($scope,$http,$window,$location) {

     $scope.listado = {
         numberOfElements: 0,
         number: 0,
         totalElements: 0
     };
     $scope.maxSize = 100;
     $scope.listadoCodigo = {};
     $scope.articulo = {};
     $scope.codigo = {};
     $scope.ipp = 20;
     $scope.pageNumber = 1;

     $scope.vendedores = {};
     $scope.vendedor = {};

     $scope.obtenerLista = function(){
         var url = "/retiro/today?page="+($scope.pageNumber-1);
         $http.get(url)
         .success(function(data, status, headers, config) {
             $scope.listado=data;
             $scope.pageNumber = $scope.listado.number+1;
         });
     };

     $scope.buscar = function(){
         $scope.obtenerLista();
     };

     $scope.agregar = function(){
         $scope.retiro.retiroId = undefined;
         $scope.retiro.fecha = undefined;
         $scope.save(function(){
             $scope.retiro = {};
         });
     };

      $scope.modificarRetiro = function(){
          $scope.retiro.fecha = undefined;
          $scope.save(function(){
              $scope.retiro = {};
          });
      };

     $scope.save = function(callback){
         $scope.retiro.username = $scope.vendedor.username;
         $http.put("/retiro/agregar",$scope.retiro)
         .success(function(data, status, headers, config) {
             $scope.retiro=data;
             $scope.obtenerLista();
             if(callback != undefined){
                 callback();
             }
         });
     };


   $scope.obtenerListaVendedores = function(){
       var url = "/vendedores?page=0&size=1000";
       $http.get(url)
       .success(function(data, status, headers, config) {
           $scope.vendedores=data;
           /*
           if($scope.vendedores.content.length > 0){
               $scope.vendedor = $scope.vendedores.content[0];
           }
           */
       });
   };

     $scope.modificar = function(retiro){
         $scope.retiro = retiro;
     };

     $scope.init = function(){
         $scope.obtenerLista();
         $scope.obtenerListaVendedores();

     };
     $scope.init();

 };