'use strict';

describe('Controller Tests', function() {

    describe('InscripcionEvento Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInscripcionEvento, MockEvento, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInscripcionEvento = jasmine.createSpy('MockInscripcionEvento');
            MockEvento = jasmine.createSpy('MockEvento');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InscripcionEvento': MockInscripcionEvento,
                'Evento': MockEvento,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("InscripcionEventoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'asoApp:inscripcionEventoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
