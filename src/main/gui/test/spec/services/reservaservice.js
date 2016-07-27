'use strict';

describe('Service: ReservaService', function () {

  // load the service's module
  beforeEach(module('stockApp'));

  // instantiate service
  var ReservaService;
  beforeEach(inject(function (_ReservaService_) {
    ReservaService = _ReservaService_;
  }));

  it('should do something', function () {
    expect(!!ReservaService).toBe(true);
  });

});
