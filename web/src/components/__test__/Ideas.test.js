import { render, screen } from '@testing-library/react';
import Ideas from '../Ideas';

test('render the iDEAS',()=>{
    render(<Ideas/>);
    const ideasList = screen.getByTestId('ideas');
    expect(ideasList).toBeInTheDocument();
});