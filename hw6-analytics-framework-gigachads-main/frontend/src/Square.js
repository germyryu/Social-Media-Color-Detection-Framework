import React from 'react'

export class Circle extends React.Component {
  render () {
    const w = 100
    const h = 100
    const cursor = this.props.contents === ' ' ? 'crosshair' : 'not-allowed'
    const style = {
      width: w,
      height: h,
      backgroundColor: 'green',
      color: 'white',
      border: '2px solid black',
      borderRadius: '50%',
      tableLayout: 'fixed',
      cursor: cursor
    }

    return (
      <td>
        style={style}
      </td>
    )
  }
}

export class Square extends React.Component {
  render () {
    const w = 100
    const h = 100
    const cursor = this.props.contents === ' ' ? 'crosshair' : 'not-allowed'
    const style = {
      width: w,
      height: h,
      backgroundColor: this.props.color,
      color: 'white',
      border: '2px solid black',
      tableLayout: 'fixed',
      cursor: cursor
    }

    const innerContainer = {
      padding: '10px',
      border: this.props.circleBorder + ' solid ' + this.props.circleColor,
      borderRadius: '50%'
    }

    return (
      <td
        style={style}
        onClick={this.props.onClick}
      >
        <div style={innerContainer}>
          {this.props.contents}
        </div>
      </td>
    )
  }
}
