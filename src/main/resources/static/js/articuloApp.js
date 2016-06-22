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
        Articulo.getList(params).then(function(a){
             $scope.listado = a;
             $scope.pageNumber = $scope.listado.number+1;
        });

     };

     $scope.buscarArticulo = function(){
         $scope.obtenerListaArticulo();
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
     $scope.modificar = function(articulo){
         $scope.articulo = articulo;
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

        //$scope.articulo.put();

     /*
         $http.put("/articulo/agregar",$scope.articulo)
         .success(function(data, status, headers, config) {
             $scope.articulo=data;
             $scope.obtenerListaArticulo();
             if(callback != undefined){
                 callback();
             }
         });
     */
     };

     $scope.eliminar = function(articulo){
         if(confirm("Esta seguro que quiere elimiar el articulo?")){
                  Restangular.one("articulos", articulo.articuloId).remove();
         }
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