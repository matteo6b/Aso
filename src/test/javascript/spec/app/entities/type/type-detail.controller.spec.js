'use strict';

describe('Controller Tests', function() {

    describe('Type Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockType, MockAsociacion, MockEvento;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockType = jasmine.createSpy('MockType');
            MockAsociacion = jasmine.createSpy('MockAsociacion');
            MockEvento = jasmine.createSpy('MockEvento');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Type': MockType,
                'Asociacion': MockAsociacion,
                'Evento': MockEvento
            };
            createController = function() {
                $injector.get('$controller')("TypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'asoApp:typeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
