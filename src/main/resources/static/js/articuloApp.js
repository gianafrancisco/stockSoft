function articuloController($scope,$http,$window,$location, Restangular) {

     var Articulo = Restangular.all('articulos');


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

     $scope.obtenerListaArticulo = function(){

        var params = {page: $scope.pageNumber-1};
        if($scope.search != "" && $scope.search != undefined){
            params.search = $scope.search;
        }
     /*
         var url = "/articulos?page="+($scope.pageNumber-1);
         if($scope.search != "" && $scope.search != undefined){
             var search = "&search="+$scope.search;
             url = "/articulo/search?page="+($scope.pageNumber-1)+search;
         }
         $http.get(url)
         .success(function(data, status, headers, config) {
             $scope.listado=data;
             $scope.pageNumber = $scope.listado.number+1;
         });
         */

         Articulo.getList(params).then(function(a){
             $scope.listado = a;
             $scope.pageNumber = $scope.listado.number+1;
         });

     };

     $scope.buscarArticulo = function(){
         $scope.obtenerListaArticulo();
     };

     $scope.obtenerListaCodigo = function(articulo){
         if(articulo.articuloId != undefined){
             $http.get("/articulo/"+articulo.articuloId+"/items")
             .success(function(data, status, headers, config) {
                 $scope.listadoCodigo=data;
             });
          }
     };

     $scope.agregarArticulo = function(){
         $scope.articulo.articuloId = null;
         $scope.save(function(){
             $scope.articulo = {};
         });
     };

     $scope.modificarArticulo = function(){
         $scope.save(function(){
             $scope.articulo = {};
         });
     };

     $scope.agregarCodigo = function(){
         $scope.codigo.itemId = null;
         $scope.saveItem();
     };

     $scope.modificarCodigo = function(){
         $scope.saveItem();
     };

     $scope.modificar = function(articulo){
         $scope.articulo = articulo;
         //$scope.obtenerListaCodigo($scope.articulo);
     };

     $scope.copiar = function(articulo){
         $scope.modificar(articulo);
         $scope.articulo.articuloId = undefined;
         $scope.articulo.codigo = "";
         $scope.articulo.cantidadStock = 0;
         $scope.articulo.cantidad = 0;
         var element = $window.document.getElementById('codigo');
         if(element){
             element.focus();
         }
     };

     $scope.save = function(callback){
         $http.put("/articulo/agregar",$scope.articulo)
         .success(function(data, status, headers, config) {
             $scope.articulo=data;
             $scope.obtenerListaArticulo();
             if(callback != undefined){
                 callback();
             }
         });
     };

     $scope.eliminar = function(articulo){

         if(confirm("Esta seguro que quiere elimiar el articulo?")){
                  //Articulo.remove(articulo.articuloId).then(function(a){
                    //a.remove();
                    //$scope.obtenerListaArticulo();
                  //});
                  Restangular.one("articulos", articulo.articuloId).remove();
/*
             $http.get("/articulo/delete/"+articulo.articuloId)
             .success(function(data, status, headers, config) {
                 $scope.obtenerListaArticulo();
                 //$scope.obtenerListaCodigo(articulo);
             });*/
         }
     };

     $scope.eliminarCodigo = function(articulo,item){

         var a = Articulo.get(item.itemId);
         a.remove();

         /*$http.get("/articulo/"+articulo.articuloId+"/item/delete/"+item.itemId)
         .success(function(data, status, headers, config) {
             //$scope.obtenerListaCodigo(articulo);
         });*/
     };

     $scope.saveItem = function(){
         $http.put("/articulo/"+$scope.articulo.articuloId+"/agregar",$scope.codigo)
         .success(function(data, status, headers, config) {
             $scope.codigo=data;
             //$scope.obtenerListaCodigo($scope.articulo);
         });
     };

     $scope.init = function(){
         $scope.obtenerListaArticulo();

     };

     $scope.isModificable = function(){
         return $scope.articulo.articuloId == undefined;
     };

     $scope.cerrarSession = function(){
         $http.post('/logout', {}).success(function() {
             $location.path("/index.html");
         }).error(function(data) {
         });
     };
     $scope.init();

 };