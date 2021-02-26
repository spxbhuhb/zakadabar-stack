## Use Cases

In a form, a select field have to pop up the possible choices. The GUI has to restrict the choice display to the
viewport, so the user cannot scroll the underlying form by scrolling the list.

The function has to:

* calculate size,
* position horizontally,
* position vertically.

These three are interlinked as the preferred position may collide with size constraints.

Constraints for the select option list:

* if we can show at least 5 entries downwards
    * then
        * align top-left of the popup to the top-left of the form field
        * restrict size to viewport
    * else
        * align bottom-left of the list to the bottom-left of the form field
        * restrict size to the viewport

Functional requirements:

* we want to show the popup when the form field gets focus (click or tab)
* we want to close the popup when
    * one of the items is selected (click or enter)
    * user hits escape
    * user clicks outside the popup