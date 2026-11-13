    package Models;

    public class StockItem {
        private int productId;
        private int quantityOnHand;
        private int reorderLevel;

        public StockItem(int productId, int quantityOnHand, int reorderLevel) {
            this.productId = productId;
            this.quantityOnHand = quantityOnHand;
            this.reorderLevel = reorderLevel;
        }

        public int getProductId() {
            return productId;
        }

        public int getQuantityOnHand() {
            return quantityOnHand;
        }

        public int getReorderLevel() {
            return reorderLevel;
        }

        public void setQuantityOnHand(int quantityOnHand) {
            this.quantityOnHand = quantityOnHand;
        }

        public void setReorderLevel(int reorderLevel) {
            this.reorderLevel = reorderLevel;
        }

        public void increaseQuantity(int quantity) {
            quantityOnHand += quantity;
        }

        public boolean decreaseQuantity(int quantity) {
            if (quantity > quantityOnHand) {
                return false;
            }
            quantityOnHand -= quantity;
            return true;
        }

        public boolean isLowStock() {
            return quantityOnHand <= reorderLevel;
        }
    }