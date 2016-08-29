'use strict';

/**
 * @ngdoc function
 * @name stockApp.controller:DashboardCtrl
 * @description
 * # DashboardCtrl
 * Controller of the stockApp
 */
angular.module('stockApp')
  .controller('DashboardController', function ($scope, Restangular, $timeout) {
     var Reservas = Restangular.service('reservas');

     $scope.reserva = {
        descripcion: "",
        email: "",
        items: []
     };
     
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
     $scope.cantidad = 0;
     $scope.descripcionError = false;
     $scope.emailError = false;
     $scope.messageSuccess = "";
     $scope.successShow = false;

     $scope.showSuccess = function (message, show, timeout){
        $scope.messageSuccess = message;
        $scope.successShow = show;
        if(show == true){
            $timeout($scope.showSuccess, timeout, true, "", false, 10);
        }
     };

     $scope.buscar = function(buscar){

        var params = {page: $scope.pageNumber-1, sort: 'id,desc'};
        if(buscar !== "" && buscar !== undefined){
            params.search = buscar;
        }
        Reservas.getList(params).then(function(a){
             $scope.listado = a;
             $scope.pageNumber = $scope.listado.number+1;
             $scope.listado.forEach(function(current, index, array){

                var promise = new Promise(function(done, error){
                    console.log(current);

                    current.getList("items").then(function(items){
                        done({"r": current, "i": items});
                    });
                }).then(function(data){
                    data.r.resumen = data.i;
                    console.info(data.r);
                    console.info(data.i);
                    $scope.$apply();
                });
             });
        });

     };

     //TODO: Check if all the items are FISICO and not VIRTUAL

     $scope.cancelar = function(reserva){
        if(confirm("Quiere cancelar la reserva?") === true){
     	  reserva.estado = "CANCELADA";	
     	  reserva.put();
        }
     };

     $scope.cerrar = function(reserva){
        if(confirm("Quiere cerrar la reserva?") === true){
     	  reserva.estado = "CERRADA";	
     	  reserva.put();
        }
     };

     $scope.init = function(){
        $scope.buscar("");
     };

     $scope.isModificable = function(){
         return ($scope.articulo.articuloId === null || $scope.articulo.articuloId === undefined);
     };

     $scope.mostrarAcciones = function(reserva){
     	return reserva.estado === "ACTIVA" || reserva.estado === "CONFIRMADA";
     };

     $scope.mostrarResumenItems = function(items){

        if(items == undefined){return "";}

        var summary = [];
        
        items.forEach(function (current,index){
            var i = -1;
            summary.forEach(function(cur, index2){
                console.log(cur);
                console.log(current);
                if(cur.articulo.articuloId === current.articulo.articuloId){
                    i = index2;
                    return;
                }
            });
            if(i === -1){
                summary.push({"articulo": current.articulo, "cant": 1});
            }else{
                summary[i].cant++;
            }
        });
        var text = "Cantidad - Codigo - Productos\n";
        summary.forEach(function(current){
            text += current.cant + " - " + current.articulo.codigo +" - " + current.articulo.descripcion+"\n";
        });
        return text;
     };
     $scope.init();
  });
