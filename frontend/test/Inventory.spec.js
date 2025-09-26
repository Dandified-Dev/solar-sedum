import {mount} from "@vue/test-utils";
import InventoryComponent from '../src/components/inventory/InventoryComponent.vue';
import confirmDialog from "@/models/confirmDialog";

describe("InventoryComponent", () => {
    const inventoryServiceMock = {
        findAll: jest.fn().mockResolvedValue([{id: 1, name: 'Test'}]),
        findByWarehouse: jest.fn(() => []),
        addItem: jest.fn().mockResolvedValue({}),
        deleteItem: jest.fn().mockResolvedValue({}),
    }


    let wrapper;
    beforeEach(async () => {
        Object.values(inventoryServiceMock).forEach(mockFunction => mockFunction.mockClear());

        wrapper = mount(InventoryComponent, {
            global: {
                provide: {
                    inventoryService: inventoryServiceMock
                }
            }
        });
        await wrapper.vm.$nextTick();
    });



    it("renders properly when mounted", () => {
        // Arrange
        const wrapper = mount(InventoryComponent);

        // Assert
        expect(wrapper.exists()).toBe(true);
    });


    it("loads data on created", async () => {
        expect(wrapper.vm.items).toEqual([]);
        expect(wrapper.vm.warehouses).toEqual([]);
        expect(wrapper.vm.filteredItems).toEqual([]);
        expect(wrapper.vm.totalValue).toBeNull();

        await wrapper.vm.$nextTick();

        expect(wrapper.vm.items).toEqual([]);
        expect(wrapper.vm.warehouses).toEqual([]);
        expect(wrapper.vm.filteredItems).toEqual([]);
        expect(wrapper.vm.totalValue).toBeNull();
    });

    it("deletes an item", async () => {
        confirmDialog.showConfirmationDialog = jest.fn().mockImplementation((name, callback) => {
            callback();
            return Promise.resolve(true);
        });

        const itemToDelete = { id: 1, name: "Item 1", inventoryId: 123 };

        await wrapper.setData({
            filteredItems: [itemToDelete],
        });

        await wrapper.vm.delete_item(itemToDelete);

        expect(inventoryServiceMock.deleteItem).toHaveBeenCalledWith(itemToDelete.inventoryId);
    });

    it("selects a warehouse and fetches inventory items", async () => {
        await wrapper.setData({ warehouses: [{ id: 1, name: "Warehouse 1" }] });
        await wrapper.vm.handleWarehouseSelected({ id: 1, name: "Warehouse 1" });

        expect(wrapper.vm.selectedWarehouse).toEqual({ id: 1, name: "Warehouse 1" });
        expect(wrapper.vm.filteredItems).toEqual([]);
    });

    it("opens the AddItemToWarehouse form when the 'Add item to warehouse' button is clicked", async () => {
        // Arrange
        await wrapper.setData({
            showAddItemToWarehouseForm: false,
            selectedWarehouse: { id: 1, name: "Warehouse 1" }
        });

        // Act
        await wrapper.vm.openAddItemToWarehouse();

        // Assert
        expect(wrapper.vm.showAddItemToWarehouseForm).toBe(true);
    });

    it("closes the AddItemToWarehouse form when the 'close-popup' event is emitted", async () => {
        // Arrange
        await wrapper.setData({ showAddItemToWarehouseForm: true });

        // Act
        await wrapper.vm.closeAddItempopup();

        // Assert
        expect(wrapper.vm.showAddItemToWarehouseForm).toBe(false);
    });
});
