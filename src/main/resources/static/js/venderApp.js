function venderController($scope,$http,$window,$location,$rootScope) {
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
   $scope.carrito = [];
   $scope.listDevolucion = [];
   $scope.pagos = [{ id: 1, value: "Efectivo"},{ id: 2, value: "Tarjeta"},{ id: 3, value: "Mixto"}];
   $scope.formaPago = $scope.pagos[0];
   $scope.montoTotal = 0.0;
   $scope.mensajeConfirmacionVenta = "";
   $scope.efectivo = 0.0;
   $scope.tarjeta = 0.0;


   $scope.limpiarBusqueda = function(){
       $scope.listado = [];
       $scope.search = "";
   };

   $scope.obtenerListaArticulo = function(){
       if($scope.search != ""){
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
       }else{
            $scope.limpiarBusqueda();
       }
   };

   $scope.buscarArticuloDevolucion = function(){
       var url = "/venta/devolucion?codigoDevolucion="+$scope.codigoDevolucion;
       $http.get(url)
       .success(function(data, status, headers, config) {
           $scope.listadoDevolucion=data;
           $scope.listadoDevolucion.forEach(function(current){
                current.fecha = undefined;
            });
       });
   };

   $scope.buscarArticulo = function(){
       $scope.obtenerListaArticulo();
   };

   $scope.obtenerListaVendedores = function(){
       var url = "/vendedores?page=0&size=1000";
       $http.get(url)
       .success(function(data, status, headers, config) {
           $scope.vendedores=data;
           /*if($scope.vendedores.content.length > 0){
               $scope.vendedor = $scope.vendedores.content[0];
           }*/
       });
   };

   $scope.agregarCarrito = function(articulo){
       var existe = false;
       $scope.carrito.forEach(function(current,index,array){
           if(current.articulo.codigo == articulo.codigo ){
               if(current.articulo.cantidadStock > current.cantidad){
                   current.cantidad++;
               }
               existe = true;
           }
       });
       if(!existe && articulo.cantidadStock > 0){
           $scope.carrito.push({
               cantidad: 1,
               descuento: 0,
               articulo: articulo,
               vendedor: $scope.vendedor,
               formaPago: $scope.formaPago.value,
               nroCupon: $scope.numeroCupon
           });
       }
       $scope.calcularTotal();
       $scope.limpiarBusqueda();
   }



   $scope.agregarDevolucion = function(venta){
       var existe = false;
       $scope.listDevolucion.forEach(function(current,index,array){
           if(current.venta.ventaId == venta.ventaId ){
               existe = true;
           }
       });
       if(!existe){
           $scope.listDevolucion.push({
               cantidad: 1,
               venta: venta,
               vendedor: $scope.vendedor,
               formaPago: $scope.formaPago.value,
               nroCupon: $scope.numeroCupon
           });
       }
       $scope.calcularTotal();
   }
   $scope.removerCarrito = function(articulo){
       var eliminar = -1;
       $scope.carrito.forEach(function(current,index,array){
           if(current.articulo.codigo == articulo.codigo){
               current.cantidad--;
               if(current.cantidad == 0){
                   eliminar = index;
               }
           }
       });
       if(eliminar >= 0){
           $scope.carrito.splice(eliminar,1);
       }
       $scope.calcularTotal();
   }

  $scope.removerDevolucion = function(venta){
      var eliminar = -1;
      $scope.listDevolucion.forEach(function(current,index,array){
          if(current.venta.ventaId == venta.ventaId){
              current.cantidad--;
              if(current.cantidad == 0){
                  eliminar = index;
              }
          }
      });
      if(eliminar >= 0){
          $scope.listDevolucion.splice(eliminar,1);
      }
      $scope.calcularTotal();
  }

   $scope.calcularTotal = function(){
       var total = 0;
       $scope.carrito.forEach(function(current){
           total+=current.cantidad*current.articulo.precio*(1-current.descuento/100);
       });

       $scope.listDevolucion.forEach(function(current){
           total-=current.cantidad*current.venta.precio;
       });
       $scope.montoTotal = total;
       $scope.actualizarTipoPago();
   };

   $scope.datosVendedor=function(){
       return $scope.vendedor.nombre+" "+$scope.vendedor.apellido;
   };

   $scope.actualizarTipoPago = function (){
         if( $scope.formaPago.id == 1 || $scope.formaPago.id == 3 ){
             $scope.efectivo = $scope.montoTotal;
             $scope.tarjeta = 0;
         }else if( $scope.formaPago.id == 2 ){
             $scope.efectivo = 0;
             $scope.tarjeta = $scope.montoTotal;
         }
   };

   $scope.actualizarCarrito = function(){
       $scope.carrito.forEach(function(current){
           current.vendedor = $scope.vendedor;
           current.formaPago = $scope.formaPago.value;
           current.nroCupon = $scope.numeroCupon;
       });

      $scope.listDevolucion.forEach(function(current){
          current.vendedor = $scope.vendedor;
          current.formaPago = $scope.formaPago.value;
          current.nroCupon = $scope.numeroCupon;
      });

      $scope.actualizarTipoPago();

   };



   $scope.actualizarTarjeta = function (){

        if($scope.efectivo > $scope.montoTotal){
            $scope.efectivo = $scope.montoTotal;
        }

        $scope.tarjeta = $scope.montoTotal - $scope.efectivo;
   };

   $scope.vender = function(){

       if($scope.efectivo + $scope.tarjeta === $scope.montoTotal){

           if(confirm("Esta seguro de realizar la venta?")){

               var efectivo = $scope.efectivo;
               var tarjeta = $scope.tarjeta;

               $scope.actualizarCarrito();

               $http.put("/venta/confirmar",{
                    articulos: $scope.carrito,
                    devoluciones: $scope.listDevolucion,
                    efectivo: efectivo,
                    tarjeta: tarjeta,
                    formaPago: $scope.formaPago.value
               })
               .success(function(data, status, headers, config) {
                   $scope.carrito = [];
                   $scope.listDevolucion = [];
                   $scope.calcularTotal();
                   $scope.limpiarBusqueda();
                   $scope.numeroCupon = "";
                   $scope.mensajeConfirmacionVenta = data.codigoDevolucion;
                   $scope.listadoDevolucion = [];
                   $('#confirmacionVenta').modal('show');
                   $scope.efectivo = 0.0;
                   $scope.tarjeta = 0.0;
               });
           }
       }else {
            alert("Verifique que el importe a pagar con tarjeta y con efectivo sea igual al monto total de venta");
       }

   };

   $scope.cancelar = function(){

       if(confirm("Esta seguro de cancelar la venta?")){
           $scope.carrito = [];
           $scope.listDevolucion = [];
           $scope.calcularTotal();
           $scope.numeroCupon = "";
       }


   };

   $scope.notaCredito = function(){

        var search = "";
        url = "/articulo/search?page=0&size=100&search=N.CREDITO";

        $http.get(url)
        .success(function(data, status, headers, config) {
            var monto = new Number($scope.montoTotal);
            if(monto < 0){
                monto = -1*monto;
                var notaCredito = data.content[0];
                notaCredito.precioLista = monto;
                notaCredito.precio = monto;
                notaCredito.cantidadStock = 1;
                notaCredito.cantidad = 1;
                $scope.agregarCarrito(notaCredito);
            }
        });
   };

   $scope.init = function(){
       $scope.obtenerListaVendedores();
   };

   $scope.devolucion = function(){
        $('#dialogDevolucion').modal('show');
   };

   $scope.devolucionCerrar = function(){
       $('#dialogDevolucion').modal('close');
   };

   $scope.init();

}