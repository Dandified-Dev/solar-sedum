import {mount} from '@vue/test-utils';
import WarehouseComponent from '../src/components/warehouses/WarehouseComponent.vue';
import confirmDialog from "@/models/confirmDialog";

// Arrange
const warehousesServiceMock = {
    findAll: jest.fn().mockResolvedValue([{id: 1, name: 'Test Warehouse', location: 'Utrecht'}]),
    add: jest.fn().mockResolvedValue({}),
    deleteById: jest.fn().mockResolvedValue({}),
    removeTeam: jest.fn().mockResolvedValue({}),
    save: jest.fn().mockResolvedValue(true),
};

let wrapper;

// Arrange
beforeEach(async () => {
    // Clearing the mock functions before each test
    Object.values(warehousesServiceMock).forEach(mockFunction => mockFunction.mockClear());

    // Mounting the WarehouseComponent with the mocked warehouses service
    wrapper = mount(WarehouseComponent, {
        global: {
            provide: {
                warehousesService: warehousesServiceMock
            }
        }
    });
    await wrapper.vm.$nextTick();
});

describe('Warehouse', () => {
    // This test checks if the component renders correctly.
    // Fast: quick check for render
    it('Renders correctly', () => {
        // Check if the component exists
        // Assert: Check if the component exists
        expect(wrapper.exists()).toBe(true);
    });

    // This test verifies that the component fetches and displays warehouses on creation.
    // Repeatable: can be used continuously without affecting anything.
    it('Fetches and displays warehouses on created', async () => {
        // Call for the findAll function
        // Act: Call for the findAll function
        expect(warehousesServiceMock.findAll).toHaveBeenCalled();

        // Assert: check if the findAll has returned all the warehouses in the list
        expect(wrapper.vm.warehouses).toEqual([{id: 1, name: 'Test Warehouse', location: 'Utrecht'}]);
        expect(wrapper.vm.filteredWarehouses).toEqual([{id: 1, name: 'Test Warehouse', location: 'Utrecht'}]);
    });

    // This test checks if the component adds a warehouse correctly.
    // Isolated: Data is not actually stored and doesn't affect anything else
    it('Adds a warehouse correctly', async () => {
        // Act: add a warehouse to the list
        await wrapper.vm.add_warehouse({id: 2, name: 'New Warehouse', location: 'Arnhem'});
        await wrapper.vm.$nextTick();

        // Assert: check if the add function has been called and if the warehouse has been added
        expect(warehousesServiceMock.add).toHaveBeenCalledWith({id: 2, name: 'New Warehouse', location: 'Arnhem'});
        expect(wrapper.vm.filteredWarehouses).toEqual([
            {id: 1, name: 'Test Warehouse', location: 'Utrecht'},
            {id: 2, name: 'New Warehouse', location: 'Arnhem'}
        ]);
    });

    // This test checks if the component deletes a warehouse correctly.
    it('Deletes a warehouse correctly', async () => {
        // Act: delete a warehouse and confirm the verification
        confirmDialog.showConfirmationDialog = jest.fn().mockImplementation((name, callback) => {
            callback();
            return Promise.resolve(true);
        });
        await wrapper.vm.delete_warehouse({id: 1, name: 'Test Warehouse'});

        // Assert: check if the warehouse has been deleted
        expect(warehousesServiceMock.deleteById).toHaveBeenCalledWith(1);
    });

    // This test checks if the component saves warehouse location correctly.
    // Self-verifying: immediately showcases if it has gone correctly
    it('Saves warehouse location correctly', async () => {
        // Act: save the new location
        await wrapper.vm.savedLocation({id: 1, location: 'New Location'});
        await wrapper.vm.$nextTick();

        // Assert: check if the save function has been called in the mock
        expect(warehousesServiceMock.save).toHaveBeenCalledWith({id: 1, location: 'New Location'});
    });

    // This test checks if the component handles warehouse search correctly.
    // Isolated: Data of the test is independent of everything else
    it('Handles warehouse search correctly', async () => {
        // Arrange: set up initial data
        wrapper.setData({
            warehouses: [
                { id: 1, name: 'Warehouse A', location: 'City A' },
                { id: 2, name: 'Warehouse B', location: 'City B' },
                { id: 3, name: 'Warehouse C', location: 'City C' }
            ],
            filteredWarehouses: []
        });

        // Act: trigger a search for warehouses with the term 'B'
        wrapper.vm.handleSearch('B');

        // Assert: if the component correctly updates filteredWarehouses based on the search term
        expect(wrapper.vm.filteredWarehouses).toEqual([
            { id: 2, name: 'Warehouse B', location: 'City B' }
        ]);
    });


});



