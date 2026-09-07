export interface NavigationItem {

  /**
   * Display label of the navigation item.
   */
  label: string;

  /**
   * Material icon name.
   */
  icon: string;

  /**
   * Route of the menu item.
   *
   * Optional because a parent dropdown
   * may only contain children.
   */
  route?: string;

  /**
   * Roles allowed to see this item.
   */
  roles: string[];

  /**
   * Optional badge.
   *
   * Example:
   * 3
   * NEW
   * 12
   */
  badge?: string | number;

  /**
   * Child navigation items.
   *
   * Supports hierarchical menus.
   */
  children?: NavigationItem[];
}