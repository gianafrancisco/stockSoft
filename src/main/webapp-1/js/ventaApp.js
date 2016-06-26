function ventaController($scope,$http,$window,$location) {

  $scope.listado = {
      numberOfElements: 0,
      number: 0,
      totalElements: 0
  };
  $scope.maxSize = 100;
  $scope.vendedor = {};
  $scope.codigo = {};
  $scope.ipp = 20;
  $scope.pageNumber = 1;
  $scope.startTime = new Date();
  $scope.endTime = new Date();
  $scope.dateOptions = {
      formatYear: 'yy',
      startingDay: 1
    };

  $scope.dtStartTime = {
      opened: false
  };

  $scope.dtEndTime = {
          opened: false
  };

  $scope.openStartTime = function($event){
      $scope.dtStartTime.opened = true;
  };
  $scope.openEndTime = function($event){
      $scope.dtEndTime.opened = true;
  };

  $scope.obtenerLista = function(){

      if($scope.endTime == undefined){
          $scope.endTime = new Date();
      }
      if($scope.startTime == undefined){
          $scope.startTime = new Date();
      }

      $scope.startTime.setMinutes(0);
      $scope.startTime.setSeconds(0);
      $scope.startTime.setHours(0);
      $scope.startTime.setMilliseconds(0);

      $scope.endTime.setMinutes(59);
      $scope.endTime.setSeconds(59);
      $scope.endTime.setHours(23);
      $scope.endTime.setMilliseconds(999);


      var startTime=$scope.convertTime($scope.startTime);
      var endTime=$scope.convertTime($scope.endTime);


      var url = "/ventas?page="+($scope.pageNumber-1)+"&startTime="+startTime+"&endTime="+endTime;
      if($scope.search != "" && $scope.search != undefined){
          var search = "&search="+$scope.search+"&startTime="+startTime+"&endTime="+endTime;
          url = "/venta/search?page="+($scope.pageNumber-1)+search;
      }
      $http.get(url)
      .success(function(data, status, headers, config) {
          $scope.listado=data;
          $scope.pageNumber = $scope.listado.number+1;
      });
  };

  $scope.buscar = function(){
      $scope.obtenerLista();
  };

  $scope.init = function(){
      $scope.obtenerLista();
  };

  $scope.cerrarSession = function(){
      $http.post('/logout', {}).success(function() {
          $location.path("/index.html");
      }).error(function(data) {
      });
  };

  $scope.convertTime = function (time){
      return time.getFullYear()+"-"+
          ("0"+(time.getMonth()+1)).slice(-2)+"-"+
          ("0"+time.getDate()).slice(-2)+"T"+
          ("0"+time.getHours()).slice(-2)+":"+
          ("0"+time.getMinutes()).slice(-2)+":"+
          ("0"+time.getSeconds()).slice(-2)+"Z";
  };

  $scope.init();

}