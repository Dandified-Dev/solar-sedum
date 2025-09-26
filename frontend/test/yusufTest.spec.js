import {mount} from '@vue/test-utils';
import DashboardComponent from "@/components/dashboard/DashboardComponent.vue";

// Mocking the services for testing
const warehousesServiceMock = {
    findAll: jest.fn().mockResolvedValue([{id: 1, name: 'Test Warehouse', location: 'Utrecht'}]),
    add: jest.fn().mockResolvedValue({}),
    deleteById: jest.fn().mockResolvedValue({}),
    removeTeam: jest.fn().mockResolvedValue({}),
    save: jest.fn().mockResolvedValue(true),
};

const forecastServiceMock = {
    findAll: jest.fn().mockResolvedValue([
        {
            id: 1,
            orderQuantity: 2,
            stock: 20,
            quantityAfter: 18,
            date: "2023-12-28T23:00:00.000+00:00",
            orders: {
                id: 46,
                project: {
                    id: 52,
                    name: "Park Amsterdam",
                    location: "amsterdam",
                    description: "Dirty water",
                    status: "UNSTARTED",
                    warehouse: 3152,
                    startDate: "2023-12-28T23:00:00.000+00:00",
                    dueAt: "2024-01-20T00:00:00.000+00:00",
                    teamId: 32
                },
                inventory: {
                    id: 417,
                    quantity: 20,
                    max_quantity: 200,
                    status: "LOW",
                    low_stock_limit: 50,
                    warehouseId: 3152,
                    productId: 5,
                    pending: false
                },
                quantity: 2,
                status: "REQUESTED"
            }
        }
    ]),
};

const projectServiceMock = {
    findAll: jest.fn().mockResolvedValue([{}]),
}


let wrapper;

beforeEach(async () => {
    // Clearing the mock functions before each test
    Object.values(warehousesServiceMock).forEach(mockFunction => mockFunction.mockClear());
    Object.values(forecastServiceMock).forEach(mockFunction => mockFunction.mockClear());



    // Mounting the DashboardComponent with the mocked services
    wrapper = mount(DashboardComponent, {
        global: {
            provide: {
                warehousesService: warehousesServiceMock,
                forecastServiceMock: forecastServiceMock,
                projectServiceMock: projectServiceMock
            }
        }
    });
    await wrapper.vm.$nextTick();
});

describe('Dashboard', () => {

    it('renders correctly', () => {
        // AAA: No specific arrangement, acting, or assertion required for this test.
        expect(wrapper.exists()).toBe(true);
    });

    // Test 1: Checks if the warehouses are loaded correctly on component creation.
    it('loads warehouses on component creation', async () => {
        // AAA: Arrange
        await wrapper.vm.$nextTick();

        // AAA: Act
        expect(warehousesServiceMock.findAll).toHaveBeenCalled();

        // AAA: Assert
        expect(wrapper.vm.warehouses).toHaveLength(1); // Assuming one warehouse is mocked
    });

    // Test 2: Checks if the inventory list is displayed when a warehouse is selected.
    it('displays inventory list when a warehouse is selected', async () => {
        // AAA: Arrange
        const warehouse = { teams: [], inventory: [{ productId: 1, status: 'FULL', quantity: 10 }] };
        wrapper.setData({ selectedDashboard: warehouse, showAllWarehouses: false });

        // AAA: Act
        await wrapper.vm.$nextTick();

        // AAA: Assert
        expect(wrapper.find('.maind4 .inventory-list').exists()).toBe(true);
    });

    // Test 3: Checks if the correct number of forecast items is displayed.
    it('displays correct number of forecast items', async () => {
        // AAA: Arrange
        await wrapper.vm.$nextTick();

        // AAA: Act
        const forecastItems = wrapper.findAll('.maind6 .forecast-list li');

        // AAA: Assert
        expect(forecastItems).toHaveLength(wrapper.vm.forecasting.length);
    });

    // Test 4: Checks if selecting a dashboard updates the selectedDashboard property.
    it('updates selectedDashboard on dashboard selection', async () => {
        // AAA: Arrange
        const dashboardTitle = 'Test Warehouse';

        // AAA: Act
        await wrapper.vm.$nextTick();
        wrapper.vm.handleDashboardSelected(dashboardTitle);

        // AAA: Assert
        expect(wrapper.vm.selectedDashboard).toEqual({id: 1, name: 'Test Warehouse', location: 'Utrecht'});
    });

    // Test 5: Checks if selecting "All Warehouses" updates showAllWarehouses property.
    it('updates showAllWarehouses on selecting "All Warehouses"', async () => {
        // AAA: Arrange

        // AAA: Act
        await wrapper.vm.$nextTick();
        wrapper.vm.handleAllWarehousesSelected();

        // AAA: Assert
        expect(wrapper.vm.showAllWarehouses).toBe(true);
    });

    // EXTRA Test 6: Checks if the forecasting data is loaded correctly on component creation.
    it('loads forecasting data on component creation', async () => {
        // AAA: Arrange
        Object.values(forecastServiceMock).forEach(mockFunction => mockFunction.mockClear());

        // AAA: Act
        await wrapper.vm.$nextTick();

        // AAA: Assert
        expect(forecastServiceMock.findAll).toHaveBeenCalled();
        expect(wrapper.vm.forecast).toHaveLength(1); // Assuming one forecast item is mocked
    });
});
