'use strict';

/**
 * @ngdoc service
 * @name stockApp.ReservaService
 * @description
 * # ReservaService
 * Service in the stockApp.
 */
angular.module('stockApp')
  .service('ReservaService', function () {
    // AngularJS will instantiate a singleton by calling "new" on this function
    var service = {};
    service.descripcion = "";
    service.email = "";
    service.items = [];
    
    return service;
  });
