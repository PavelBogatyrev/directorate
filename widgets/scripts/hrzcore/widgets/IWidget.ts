/**
 *
 * Describes widget of HRZCore
 * Default constructor for such widget is constructor(HTMLElement) , where htmlelement is a container for widget
 * @author rlapin
 */

interface IWidget{
    setData(data: any): void;
    setConfig(config: any): void;
    render(): void;
}
